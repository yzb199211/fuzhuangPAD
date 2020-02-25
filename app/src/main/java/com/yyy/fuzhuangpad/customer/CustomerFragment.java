package com.yyy.fuzhuangpad.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.adapter.FormAdapter;
import com.yyy.fuzhuangpad.color.ColorBeans;
import com.yyy.fuzhuangpad.dialog.LoadingDialog;
import com.yyy.fuzhuangpad.interfaces.OnItemClickListener;
import com.yyy.fuzhuangpad.interfaces.OnSelectClickListener;
import com.yyy.fuzhuangpad.interfaces.ResponseListener;
import com.yyy.fuzhuangpad.popwin.Popwin;
import com.yyy.fuzhuangpad.util.CodeUtil;
import com.yyy.fuzhuangpad.util.PxUtil;
import com.yyy.fuzhuangpad.util.SharedPreferencesHelper;
import com.yyy.fuzhuangpad.util.StringUtil;
import com.yyy.fuzhuangpad.util.Toasts;
import com.yyy.fuzhuangpad.util.net.NetConfig;
import com.yyy.fuzhuangpad.util.net.NetParams;
import com.yyy.fuzhuangpad.util.net.NetUtil;
import com.yyy.fuzhuangpad.view.button.ButtonSelect;
import com.yyy.fuzhuangpad.view.button.ButtonWithImg;
import com.yyy.fuzhuangpad.view.form.FormColumn;
import com.yyy.fuzhuangpad.view.form.FormRow;
import com.yyy.fuzhuangpad.view.recycle.RecyclerViewDivider;
import com.yyy.fuzhuangpad.view.search.SearchEdit;
import com.yyy.yyylibrary.pick.builder.OptionsPickerBuilder;
import com.yyy.yyylibrary.pick.listener.OnOptionsSelectListener;
import com.yyy.yyylibrary.pick.view.OptionsPickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CustomerFragment extends Fragment {
    private final int formTitleId = 0x00001000;
    @BindView(R.id.se_code)
    SearchEdit seCode;
    @BindView(R.id.se_name)
    SearchEdit seName;
    @BindView(R.id.bwi_remove)
    ButtonWithImg bwiRemove;
    @BindView(R.id.bwi_search)
    ButtonWithImg bwiSearch;
    @BindView(R.id.ll_search)
    RelativeLayout llSearch;
    @BindView(R.id.ll_btn)
    LinearLayout llBtn;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.bs_type)
    ButtonSelect bsType;

    FormRow formTitle;
    List<FormColumn> titles;
    private List<CustomerBeans> customerDatas;
    private List<List<FormColumn>> formDatas;
    private List<CustomerType> customerTypes;

    private String url;
    private String address;
    private String companyCode;
    private String customerType = "";
    private String customerId = "";
    private String customerName = "";
    private String filter = "";

    private SharedPreferencesHelper preferencesHelper;
    private FormAdapter formAdapter;
    private boolean isFrist = true;
    private OptionsPickerView pvType;
    private RecyclerView recyclerView;
    private Popwin popType;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesHelper = new SharedPreferencesHelper(getActivity(), getString(R.string.preferenceCache));
        initDefaultData();
        initList();
        getData();
    }

    private void initDefaultData() {
        address = (String) preferencesHelper.getSharedPreference("address", "");
        companyCode = (String) preferencesHelper.getSharedPreference("companyCode", "");
        url = address + NetConfig.server + NetConfig.MobileAppHandler_Method;
    }

    private void getData() {
        LoadingDialog.showDialogForLoading(getActivity());
        new NetUtil(getParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initFormData(string);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingFinish(getActivity().getString(R.string.error_json));
                }
            }

            @Override
            public void onFail(IOException e) {
                e.printStackTrace();
                LoadingFinish(e.getMessage());
            }
        });
    }


    private List<NetParams> getParams() {
        List<NetParams> list = new ArrayList<>();
        list.add(new NetParams("sCompanyCode", companyCode));
        list.add(new NetParams("otype", "GetTableData"));
        list.add(new NetParams("sTableName", "vwBscDataCustomer"));
        list.add(new NetParams("sFields", "iRecNo,sCustID,sCustName,sCustShortName,sClassID,sClassName,sSaleID,sSaleName,sPerson,sTel,sAddress,dStopDate,sRemark,iCustType"));
        list.add(new NetParams("sFilters", getFilter()));
        return list;
    }

    private String getFilter() {
        filter = "iCustType=0";
        if (!isFrist) {
            customerId = seCode.getText();
            customerName = seName.getText();
            if (StringUtil.isNotEmpty(customerType)) {
                filter = filter + " and " + "sClassName=" + "\'" + customerType + "\'";
            }
            if (StringUtil.isNotEmpty(customerId)) {
                filter = filter + " and " + "sCustID like" + "\'|" + customerId + "|\'";
            }
            if (StringUtil.isNotEmpty(customerName)) {
                filter = filter + " and " + "sCustShortName like" + "\'|" + customerName + "|\'";
            }
        } else {
            isFrist = false;
        }
        return filter;
    }

    private void initFormData(String string) throws JSONException {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setListData(jsonObject.optJSONObject("dataset").optString("vwBscDataCustomer"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setListData(String data) {
        if (StringUtil.isNotEmpty(data)) {
            customerDatas.addAll(new Gson().fromJson(data, new TypeToken<List<CustomerBeans>>() {
            }.getType()));
            setFormData();
        } else {
            LoadingFinish(getString(R.string.error_empty));
        }
    }

    private void setFormData() {
        getFormData();
        setData();
    }


    private void getFormData() {
        for (int i = 0; i < customerDatas.size(); i++) {
            CustomerBeans customer = customerDatas.get(i);
            customer.setRow(i);
            formDatas.add(customer.getList());
        }
    }

    private void setData() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (recyclerView == null) {
                    initRecycleView();
                }
                refreshList();
                LoadingFinish(null);
            }
        });
    }

    private void refreshList() {
        if (formAdapter == null) {
            initFormAdapter();
        } else {
            formAdapter.notifyDataSetChanged();
        }
    }

    private void initFormAdapter() {
        formAdapter = new FormAdapter(formDatas, getActivity());
        formAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                go2Detail(pos);
            }
        });
        recyclerView.setAdapter(formAdapter);
    }


    private void initRecycleView() {
        recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecyclerViewDivider(getActivity(), LinearLayout.VERTICAL));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, formTitleId);
        recyclerView.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
        rlMain.addView(recyclerView, params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        initTitle();
        initView();
    }

    private void initView() {
        initTypeView();
    }

    private void initTypeView() {
        bsType.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (customerTypes.size() == 0) {
                    getType();
                } else {
                    popType.showAsDropDown(bsType.getTvContent());
//                    pvType.show();
                }
            }
        });
    }


    private void initList() {
        titles = new ArrayList<>();
        customerDatas = new ArrayList<>();
        customerTypes = new ArrayList<>();
        formDatas = new ArrayList<>();
    }

    private void initTitle() {
        formTitle = new FormRow(getActivity()).isTitle(true).setColumns(CustomerUtil.getTitles(getActivity())).build();
        formTitle.setId(formTitleId);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getActivity().getResources().getDimensionPixelSize(R.dimen.dp_20));
        params.addRule(RelativeLayout.BELOW, R.id.ll_btn);
        params.topMargin = getActivity().getResources().getDimensionPixelSize(R.dimen.dp_5);
        rlMain.addView(formTitle, params);
    }


    @OnClick({R.id.bwi_remove, R.id.bwi_search, R.id.bwi_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bwi_remove:
                clearFilter();
                break;
            case R.id.bwi_search:
                refreshData();
                break;
            case R.id.bwi_add:
                go2Detail(-1);
                break;
            default:
                break;
        }
    }

    private void refreshData() {
        if (formAdapter != null) {
            formDatas.clear();
            customerDatas.clear();
            formAdapter.notifyDataSetChanged();
        }
        getData();
    }

    private void clearFilter() {
        seCode.clear();
        seName.clear();
        bsType.setContext("");
        customerType = "";
        refreshData();
    }

    private void getType() {
        LoadingDialog.showDialogForLoading(getActivity());
        new NetUtil(getTypeParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initTypeData(string);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingFinish(getActivity().getString(R.string.error_json));
                } catch (Exception e) {
                    e.printStackTrace();
                    LoadingFinish(getActivity().getString(R.string.error_data));
                }
            }

            @Override
            public void onFail(IOException e) {
                e.printStackTrace();
                LoadingFinish(e.getMessage());
            }
        });
    }

    private void initTypeData(String string) throws JSONException, Exception {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setTypeData(jsonObject.optJSONObject("dataset").optString("bscdataclass"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setTypeData(String optString) throws Exception {
        List<CustomerType> list = new Gson().fromJson(optString, new TypeToken<List<CustomerType>>() {
        }.getType());
        if (list == null || list.size() == 0) {
            LoadingFinish(getString(R.string.error_empty));
        } else {
            CustomerType customerType = new CustomerType();
            customerType.setsClassName(getString(R.string.common_empty));
            customerTypes.add(customerType);
            customerTypes.addAll(list);
            LoadingFinish(null);
            setPickColorType();
        }
    }

    private void setPickColorType() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                initPickColorType();
                initPopType();
            }
        });
    }

    private void initPopType() {
        popType = new Popwin(getActivity(), customerTypes);
        popType.showAsDropDown(bsType.getTvContent());
        popType.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                String type = customerTypes.get(pos).getPickerViewText();
                if (!type.equals(customerType)) {
                    customerType = type.equals(getString(R.string.common_empty)) ? "" : customerTypes.get(pos).getPickerViewText();
                    bsType.setContext(customerType);
//                    refreshData();
                }
            }
        });
    }

    private void initPickColorType() {
        pvType = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String type = customerTypes.get(options1).getPickerViewText();
                if (!type.equals(customerType)) {
                    customerType = type.equals(getString(R.string.common_empty)) ? "" : customerTypes.get(options1).getPickerViewText();
                    bsType.setContext(customerType);
                }
            }
        })
                .setTitleText("类别选择")
                .setContentTextSize(18)//设置滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .build();
        pvType.setPicker(customerTypes);//一级选择器
        setDialog(pvType);
        pvType.show();
    }

    private List<NetParams> getTypeParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "bscdataclass"));
        params.add(new NetParams("sFields", "sClassName"));
        params.add(new NetParams("sFilters", "sType='customer'"));
        return params;
    }

    private void go2Detail(int pos) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), CustomerDetailActivity.class);
        if (pos != -1)
            intent.putExtra("data", new Gson().toJson(customerDatas.get(pos))).putExtra("pos", pos);
        startActivityForResult(intent, CodeUtil.CUSTOMERDETAIL);
    }

    private void LoadingFinish(String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (StringUtil.isNotEmpty(msg)) {
                    Toast(msg);
                }
                LoadingDialog.cancelDialogForLoading();
            }
        });
    }

    private void Toast(String msg) {
        Toasts.showShort(getActivity(), msg);
    }

    private void setDialog(OptionsPickerView pickview) {
        getDialogLayoutParams();
        pickview.getDialogContainerLayout().setLayoutParams(getDialogLayoutParams());
        initDialogWindow(pickview.getDialog().getWindow());
    }

    private void initDialogWindow(Window window) {
        window.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
        window.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
        window.setDimAmount(0.1f);
        window.setAttributes(getDialogWindowLayoutParams(window));
    }

    private WindowManager.LayoutParams getDialogWindowLayoutParams(Window window) {
        WindowManager.LayoutParams winParams;
        winParams = window.getAttributes();
        winParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        return winParams;
    }

    private FrameLayout.LayoutParams getDialogLayoutParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                PxUtil.getWidth(getActivity()) / 2,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        params.leftMargin = 0;
        params.rightMargin = 0;
        return params;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (resultCode) {
                case CodeUtil.REFRESH:
                    addData(data);
                    break;
                case CodeUtil.MODIFY:
                    modifyData(data);
                    break;
                case CodeUtil.DELETE:
                    removeData(data);
                    break;
            }
        }
    }

    private void addData(Intent data) {
        CustomerBeans item = new Gson().fromJson(data.getStringExtra("style"), CustomerBeans.class);
        customerDatas.add(0, item);
        formDatas.add(0, item.getList());
        refreshList();
    }

    private void modifyData(Intent data) {
        int pos = data.getIntExtra("pos", -1);
        CustomerBeans item = new Gson().fromJson(data.getStringExtra("style"), CustomerBeans.class);
        if (pos != -1) {
            modifyCustomer(customerDatas.get(pos), item);
            modifyFormData(formDatas.get(pos), item);
        }
    }

    private void modifyCustomer(CustomerBeans customerBeans, CustomerBeans item) {
        customerBeans.copy(item);
    }

    private void modifyFormData(List<FormColumn> formColumns, CustomerBeans item) {
        formColumns.get(1).setText(item.getsCustID());
        formColumns.get(2).setText(item.getsCustShortName());
        formColumns.get(3).setText(item.getsSaleName());
        formColumns.get(4).setText(item.getsClassName());
        formColumns.get(5).setText(item.getsPerson());
        formColumns.get(6).setText(item.getsTel());
        refreshList();
    }

    private void removeData(Intent data) {
        int pos = data.getIntExtra("pos", -1);
        if (pos != -1) {
            customerDatas.remove(pos);
            formDatas.remove(pos);
            refreshList();
        }
    }

}
