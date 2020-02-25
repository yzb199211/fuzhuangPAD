package com.yyy.fuzhuangpad.sale;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.yyy.fuzhuangpad.color.ColorDetailActivity;
import com.yyy.fuzhuangpad.customer.CustomerBeans;
import com.yyy.fuzhuangpad.dialog.LoadingDialog;
import com.yyy.fuzhuangpad.interfaces.OnItemClickListener;
import com.yyy.fuzhuangpad.interfaces.OnSelectClickListener;
import com.yyy.fuzhuangpad.interfaces.ResponseListener;
import com.yyy.fuzhuangpad.popwin.Popwin;
import com.yyy.fuzhuangpad.util.CodeUtil;
import com.yyy.fuzhuangpad.util.PxUtil;
import com.yyy.fuzhuangpad.util.SharedPreferencesHelper;
import com.yyy.fuzhuangpad.util.StringUtil;
import com.yyy.fuzhuangpad.util.TimeUtil;
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
import com.yyy.yyylibrary.pick.builder.TimePickerBuilder;
import com.yyy.yyylibrary.pick.listener.OnOptionsSelectListener;
import com.yyy.yyylibrary.pick.listener.OnTimeSelectChangeListener;
import com.yyy.yyylibrary.pick.listener.OnTimeSelectListener;
import com.yyy.yyylibrary.pick.view.OptionsPickerView;
import com.yyy.yyylibrary.pick.view.TimePickerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class BillingFragment extends Fragment {
    @BindView(R.id.se_code)
    SearchEdit seCode;
    @BindView(R.id.bs_customer)
    ButtonSelect bsCustomer;
    @BindView(R.id.bwi_remove)
    ButtonWithImg bwiRemove;
    @BindView(R.id.bwi_search)
    ButtonWithImg bwiSearch;
    @BindView(R.id.ll_search)
    RelativeLayout llSearch;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.bs_date_start)
    ButtonSelect bsDateStart;
    @BindView(R.id.bs_data_end)
    ButtonSelect bsDataEnd;
    @BindView(R.id.bs_shop)
    ButtonSelect bsShop;
    @BindView(R.id.bs_status)
    ButtonSelect bsStatus;

    private final int formTitleId = 0x00001000;
    FormRow formTitle;
    List<FormColumn> titles;
    private List<List<FormColumn>> formDatas;
    private List<BillBean> billDatas;
    private List<BillShop> shops;
    private List<BillCustomer> customers;
    private List<BillStatus> status;
    private TimePickerView pvDateStart;
    private TimePickerView pvDateEnd;
    private OptionsPickerView pvShop;
    private OptionsPickerView pvCustomer;
    private OptionsPickerView pvStatus;
    private Popwin popShop;
    private Popwin popStatus;
    private String url;
    private String address;
    private String companyCode;
    private String filter = "";
    private String shop = "";
    private int shopid;
    private int statusId = 0;
    private String statusName;
    private String code;
    private String customer;
    private int customerId;
    private SharedPreferencesHelper preferencesHelper;
    private FormAdapter formAdapter;
    private RecyclerView recyclerView;
    private boolean isFrist = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesHelper = new SharedPreferencesHelper(getActivity(), getString(R.string.preferenceCache));
        initDefaultData();
        initList();
        getData();
    }

    private void getData() {
        LoadingDialog.showDialogForLoading(getActivity());
        new NetUtil(getParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    Log.e("data", string);
                    initFormData(string);
                } catch (Exception e) {
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

    private void initFormData(String string) throws JSONException {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setListData(jsonObject.optJSONObject("dataset").optString("vwSdContractM"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setListData(String data) {
        if (StringUtil.isNotEmpty(data)) {
            billDatas.addAll(new Gson().fromJson(data, new TypeToken<List<BillBean>>() {
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

    private void go2Detail(int pos) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), BillDetailActivity.class);
        if (pos != -1) {
            intent.putExtra("data", new Gson().toJson(billDatas.get(pos)));
        }
        intent.putExtra("pos", pos);
        startActivityForResult(intent, CodeUtil.BILLINGDETAIL);
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

    private void getFormData() {
        for (int i = 0; i < billDatas.size(); i++) {
            BillBean billBean = billDatas.get(i);
            billBean.setRow(i);
            formDatas.add(billBean.getList());
        }
    }

    private List<NetParams> getParams() {
        List<NetParams> list = new ArrayList<>();
        list.add(new NetParams("sCompanyCode", companyCode));
        list.add(new NetParams("otype", "GetTableData"));
        list.add(new NetParams("sTableName", "vwSdContractM"));
        list.add(new NetParams("sFields", BillBean.getFields()));
        list.add(new NetParams("sFilters", getFilter()));
        list.add(new NetParams("sSorts", ""));
        return list;
    }

    private String getFilter() {
        filter = filter + (StringUtil.isNotEmpty(filter) ? " and " : "") + "isNull(iStatus,0) =" + "\'" + statusId + "\'";
        if (!isFrist) {
            code = seCode.getText();
//            customerId = seName.getText();
            if (shopid != 0) {
                filter = filter + (StringUtil.isNotEmpty(filter) ? " and " : "") + "iBscdataStockMRecNo=" + "\'" + shopid + "\'";
//                filter = "iBscdataStockMRecNo=" + "\'" + shopid + "\'";
            }
            if (StringUtil.isNotEmpty(code)) {
                filter = filter + (StringUtil.isNotEmpty(filter) ? " and " : "") + "sOrderNo like" + "\'|" + code + "|\'";
            }
            if (customerId != 0) {
                filter = filter + (StringUtil.isNotEmpty(filter) ? " and " : "") + "iBscDataCustomerRecNo =" + "\'" + customerId + "\'";
            }
            if (StringUtil.isNotEmpty(bsDateStart.getContent())) {
                filter = filter + (StringUtil.isNotEmpty(filter) ? " and " : "") + "dDate>=convert(datetime,'" + bsDateStart.getContent() + "')";
            }
            if (StringUtil.isNotEmpty(bsDataEnd.getContent())) {
                filter = filter + (StringUtil.isNotEmpty(filter) ? " and " : "") + "dDate<=convert(datetime,'" + bsDataEnd.getContent() + "')";
            }
        } else {
            isFrist = false;
        }
        return filter;
    }

    private void initDefaultData() {
        statusName = getString(R.string.common_default_status);
        address = (String) preferencesHelper.getSharedPreference("address", "");
        companyCode = (String) preferencesHelper.getSharedPreference("companyCode", "");
        url = address + NetConfig.server + NetConfig.MobileAppHandler_Method;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_billing, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        bsStatus.setContext(statusName);
        initTitle();
        initView();
    }

    private void initList() {
        titles = new ArrayList<>();
        shops = new ArrayList<>();
        formDatas = new ArrayList<>();
        billDatas = new ArrayList<>();
        customers = new ArrayList<>();
        status = new ArrayList<>();
    }

    private void initTitle() {
        formTitle = new FormRow(getActivity()).isTitle(true).setColumns(BillingUtil.getTitles(getActivity())).build();
        formTitle.setId(formTitleId);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getActivity().getResources().getDimensionPixelSize(R.dimen.dp_20));
        params.addRule(RelativeLayout.BELOW, R.id.ll_search2);
        params.topMargin = getActivity().getResources().getDimensionPixelSize(R.dimen.dp_5);
        rlMain.addView(formTitle, params);
    }

    private void initView() {
        initStartDate();
        initEndDate();
        initShopView();
        initCustomerView();
        initStatusView();
    }

    private void initStatusView() {
        bsStatus.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (status.size() == 0) {
                    getStatus();
                } else {
                    popStatus.showAsDropDown(bsStatus.getTvContent());
                }
            }
        });
    }

    private void getStatus() {
        LoadingDialog.showDialogForLoading(getActivity());
        new NetUtil(getStatusParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initStatusData(string);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingFinish(getString(R.string.error_json));
                } catch (Exception e) {
                    e.printStackTrace();
                    LoadingFinish(getString(R.string.error_data));
                }
            }

            @Override
            public void onFail(IOException e) {
                e.printStackTrace();
                LoadingFinish(e.getMessage());
            }
        });
    }

    private void initStatusData(String string) throws JSONException, Exception {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setStatusData(jsonObject.optJSONObject("dataset").optString("bscBillstatus"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setStatusData(String optString) {
        List<BillStatus> list = new Gson().fromJson(optString, new TypeToken<List<BillStatus>>() {
        }.getType());
        if (list == null || list.size() == 0) {
            LoadingFinish(getString(R.string.error_empty));
        } else {
            status.addAll(list);
            LoadingFinish(null);
            setPickStatus();
        }
    }

    private void setPickStatus() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                initPickStatus();
                initPopStatus();
            }
        });
    }

    private void initPopStatus() {
        popStatus = new Popwin(getActivity(), status, bsStatus.getTvContent().getWidth());
        popStatus.showAsDropDown(bsStatus.getTvContent());
        popStatus.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                statusName = status.get(pos).getPickerViewText();
                statusId = status.get(pos).getiStatus();
                bsStatus.setContext(statusName);
            }
        });
    }

    private void initPickStatus() {
        pvStatus = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                statusName = status.get(options1).getPickerViewText();
                statusId = status.get(options1).getiStatus();
                bsStatus.setContext(statusName);
            }
        })
                .setTitleText("状态选择")
                .setContentTextSize(18)//设置滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .build();
        pvStatus.setPicker(status);//一级选择器
        setDialog(pvStatus);
        pvStatus.show();
    }

    private List<NetParams> getStatusParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "bscBillstatus"));
        params.add(new NetParams("sFields", "iStatus,sStatusName"));
        params.add(new NetParams("sFilters", ""));
        return params;
    }

    private void initCustomerView() {
        bsCustomer.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (customers.size() == 0) {
                    getCustomers();
                } else {
                    pvCustomer.show();
                }
            }
        });
    }

    private void getCustomers() {
        LoadingDialog.showDialogForLoading(getActivity());
        new NetUtil(getCustomerParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initCustomerData(string);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingFinish(getString(R.string.error_json));
                } catch (Exception e) {
                    e.printStackTrace();
                    LoadingFinish(getString(R.string.error_data));
                }
            }

            @Override
            public void onFail(IOException e) {
                e.printStackTrace();
                LoadingFinish(e.getMessage());
            }
        });
    }

    private void initCustomerData(String string) throws JSONException, Exception {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setCustomerData(jsonObject.optJSONObject("dataset").optString("BscDataCustomer"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setCustomerData(String optString) {
        List<BillCustomer> list = new Gson().fromJson(optString, new TypeToken<List<BillCustomer>>() {
        }.getType());
        if (list == null || list.size() == 0) {
            LoadingFinish(getString(R.string.error_empty));
        } else {
            BillCustomer billCustomer = new BillCustomer();
            billCustomer.setsCustShortName(getString(R.string.common_empty));
            customers.add(billCustomer);
            customers.addAll(list);
            LoadingFinish(null);
            setPickCustomer();
        }
    }

    private void setPickCustomer() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initPickCustomer();
            }
        });
    }

    private void initPickCustomer() {
        pvCustomer = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String type = customers.get(options1).getPickerViewText();
                if (!type.equals(customer)) {
                    customer = type.equals(getString(R.string.common_empty)) ? "" : customers.get(options1).getPickerViewText();
                    customerId = customers.get(options1).getIrecno();
                    bsCustomer.setContext(customer);
                }
            }
        })
                .setTitleText("客户选择")
                .setContentTextSize(18)//设置滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .build();
        pvCustomer.setPicker(customers);//一级选择器
        setDialog(pvCustomer);
        pvCustomer.show();
    }

    private List<NetParams> getCustomerParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "BscDataCustomer"));
        params.add(new NetParams("sFields", "irecno,sCustShortName"));
        params.add(new NetParams("sFilters", "iCustType=0"));
        return params;
    }

    private void initStartDate() {
        bsDateStart.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                pickDateStart();
            }
        });
    }

    private void initEndDate() {
        bsDataEnd.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                pickDateEnd();
            }
        });
    }

    private void initShopView() {
        bsShop.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (shops.size() == 0) {
                    getShops();
                } else {
                    popShop.showAsDropDown(bsShop.getTvContent());
                }
            }
        });
    }

    private void getShops() {
        LoadingDialog.showDialogForLoading(getActivity());
        new NetUtil(getShopParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initShopData(string);
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

    private List<NetParams> getShopParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "BscdataStockM"));
        params.add(new NetParams("sFields", "iRecNo,sStockName"));
        params.add(new NetParams("sFilters", "iShop=1"));
        return params;
    }

    private void initShopData(String string) throws JSONException, Exception {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setShopData(jsonObject.optJSONObject("dataset").optString("BscdataStockM"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setShopData(String optString) throws Exception {
        List<BillShop> list = new Gson().fromJson(optString, new TypeToken<List<BillShop>>() {
        }.getType());
        if (list == null || list.size() == 0) {
            LoadingFinish(getString(R.string.error_empty));
        } else {
            BillShop billShop = new BillShop();
            billShop.setsStockName(getString(R.string.common_empty));
            shops.add(billShop);
            shops.addAll(list);
            LoadingFinish(null);
            setPickShop();
        }
    }

    private void setPickShop() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                initPickShop();
                initPopShop();
            }
        });
    }

    private void initPopShop() {
        popShop = new Popwin(getActivity(), shops);
        popShop.showAsDropDown(bsShop.getTvContent());
        popShop.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                String type = shops.get(pos).getPickerViewText();
                if (!type.equals(shop)) {
                    shop = type.equals(getString(R.string.common_empty)) ? "" : type;
                    shopid = shops.get(pos).getiRecNo();
                    bsShop.setContext(shop);
                }
            }
        });
    }

    private void initPickShop() {
        pvShop = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String type = shops.get(options1).getPickerViewText();
                if (!type.equals(shop)) {
                    shop = type.equals(getString(R.string.common_empty)) ? "" : shops.get(options1).getPickerViewText();
                    shopid = shops.get(options1).getiRecNo();
                    bsShop.setContext(shop);
                }
            }
        })
                .setTitleText("门店选择")
                .setContentTextSize(18)//设置滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .build();
        pvShop.setPicker(shops);//一级选择器
        setDialog(pvShop);
        pvShop.show();
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

    private void clearFilter() {
        seCode.clear();
        bsCustomer.setContext("");
        bsShop.setContext("");
        bsDateStart.setContext("");
        bsDataEnd.setContext("");
        shop = "";
        shopid = 0;
        customerId = 0;
        refreshData();
    }

    private void refreshData() {
        if (formAdapter != null) {
            formDatas.clear();
            billDatas.clear();
            formAdapter.notifyDataSetChanged();
        }
        getData();
    }

    private void pickDateStart() {
        if (pvDateStart == null) {
            try {
                initPvDateStart();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pvDateStart.show();
    }

    private void pickDateEnd() {
        if (pvDateEnd == null) {
            try {
                initPvDateEnd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pvDateEnd.show();
    }

    private void initPvDateStart() throws Exception {
        pvDateStart = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                bsDateStart.setContext(StringUtil.getDate(date));
            }
        }).setRangDate(TimeUtil.str2calendar(getString(R.string.common_pickdate_start)), TimeUtil.str2calendar(getString(R.string.common_pickdate_end)))
                .setDate(Calendar.getInstance())
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .setContentTextSize(18).setBgColor(0xFFFFFFFF)
                .build();
        pvDateStart.getDialogContainerLayout().setLayoutParams(initPvTimeDialog());
//initPvTimeDialog(pvDateStart.getDialog());
        initPvTimeWindow(pvDateStart.getDialog().getWindow());
    }

    private void initPvDateEnd() throws Exception {
        pvDateEnd = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                bsDataEnd.setContext(StringUtil.getDate(date));
            }
        }).setRangDate(TimeUtil.str2calendar(getString(R.string.common_pickdate_start)), TimeUtil.str2calendar(getString(R.string.common_pickdate_end)))
                .setDate(Calendar.getInstance())
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .setContentTextSize(18).setBgColor(0xFFFFFFFF)
                .build();
        pvDateEnd.getDialogContainerLayout().setLayoutParams(initPvTimeDialog());
//        initPvTimeDialog(pvDateEnd.getDialog());
        initPvTimeWindow(pvDateEnd.getDialog().getWindow());
    }

    private void initPvTimeWindow(Window dialogWindow) {
        if (dialogWindow != null) {
            dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
            dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            dialogWindow.setDimAmount(0.1f);
            //当显示只有一列是需要设置window宽度，防止两边有空隙；
            WindowManager.LayoutParams winParams;
            winParams = dialogWindow.getAttributes();
            winParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(winParams);
        }
    }


    private FrameLayout.LayoutParams initPvTimeDialog() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                PxUtil.getWidth(getActivity()) / 2,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        params.leftMargin = 0;
        params.rightMargin = 0;
        return params;
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
//        if (resultCode == CodeUtil.REFRESH)
//            refreshData();
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
        BillBean item = new Gson().fromJson(data.getStringExtra("data"), BillBean.class);
        billDatas.add(0, item);
        formDatas.add(0, item.getList());
        refreshList();
    }

    private void modifyData(Intent data) {
        int pos = data.getIntExtra("pos", -1);
        BillBean item = new Gson().fromJson(data.getStringExtra("data"), BillBean.class);
        if (pos != -1) {
            modifyCustomer(billDatas.get(pos), item);
            modifyFormData(formDatas.get(pos), item);
        }
    }

    private void modifyCustomer(BillBean billBean, BillBean item) {
        billBean.copy(item);
    }

    private void modifyFormData(List<FormColumn> formColumns, BillBean item) {
        formColumns.get(1).setText(item.getsStatusName());
        formColumns.get(2).setText(item.getsOrderNo());
        formColumns.get(3).setText(item.getsStockName());
        formColumns.get(4).setText(item.getdDate());
        formColumns.get(5).setText(item.getsCustShortName());
        formColumns.get(6).setText(item.getsSaleName());
        formColumns.get(7).setText(item.getiQty() + "");
        formColumns.get(8).setText(item.getfTotal() + "");
        refreshList();
    }

    private void removeData(Intent data) {
        int pos = data.getIntExtra("pos", -1);
        if (pos != -1) {
            billDatas.remove(pos);
            formDatas.remove(pos);
            refreshList();
        }
    }

}