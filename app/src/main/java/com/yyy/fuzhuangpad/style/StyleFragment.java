package com.yyy.fuzhuangpad.style;


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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.adapter.FormAdapter;
import com.yyy.fuzhuangpad.dialog.LoadingDialog;
import com.yyy.fuzhuangpad.interfaces.OnItemClickListener;
import com.yyy.fuzhuangpad.interfaces.OnSelectClickListener;
import com.yyy.fuzhuangpad.interfaces.ResponseListener;
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

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class StyleFragment extends Fragment {
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
    RecyclerView recyclerView;
    List<FormColumn> titles;
    private List<StyleBean> styleDatas;
    private List<List<FormColumn>> formDatas;
    private List<StyleType> styleTypes;

    private String url;
    private String address;
    private String companyCode;
    private String styleType = "";
    private String styleId = "";
    private String styleName = "";
    private String filter = "";
    private SharedPreferencesHelper preferencesHelper;
    private FormAdapter formAdapter;
    private boolean isFrist = true;
    private OptionsPickerView pvType;

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
        list.add(new NetParams("sTableName", "vwBscDataStyleM"));
        list.add(new NetParams("sFields", "iRecNo,sStyleNo,sStyleName,sClassID,sClassName,sSizeGroupID,sGroupName,iBscDataCustomerRecNo,sCustShortName,iYear,fCostPrice,fBulkTotal1,fSalePrice,sCustStyleNo,sWaterElents,sReMark,dStopDate"));
        list.add(new NetParams("sFilters", getFilter()));
        return list;
    }

    private String getFilter() {
        filter = "";
        if (!isFrist) {
            styleId = seCode.getText();
            styleName = seName.getText();
            if (StringUtil.isNotEmpty(styleType)) {
                filter = "sClassName=" + "\'" + styleType + "\'";
            }
            if (StringUtil.isNotEmpty(styleId)) {
                filter = filter + (StringUtil.isNotEmpty(filter) ? " and " : "") + "sStyleNo like" + "\'|" + styleId + "|\'";
            }
            if (StringUtil.isNotEmpty(styleName)) {
                filter = filter + (StringUtil.isNotEmpty(filter) ? " and " : "") + "sStyleName like" + "\'|" + styleName + "|\'";
            }
        } else {
            isFrist = false;
        }
        return filter;
    }

    private void initFormData(String string) throws JSONException {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setListData(jsonObject.optJSONObject("dataset").optString("vwBscDataStyleM"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setListData(String data) {
        if (StringUtil.isNotEmpty(data)) {
            styleDatas.addAll(new Gson().fromJson(data, new TypeToken<List<StyleBean>>() {
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
        for (int i = 0; i < styleDatas.size(); i++) {
            StyleBean style = styleDatas.get(i);
            style.setRow(i);
            formDatas.add(style.getList());
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
//                Log.e("pos", pos + "");
                go2Detail(new Gson().toJson(styleDatas.get(pos)));
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
        View view = inflater.inflate(R.layout.fragment_style, container, false);
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
                if (styleTypes.size() == 0) {
                    getType();
                } else {
                    pvType.show();
                }
            }
        });
    }

    private void initList() {
        titles = new ArrayList<>();
        styleDatas = new ArrayList<>();
        formDatas = new ArrayList<>();
        styleTypes = new ArrayList<>();
    }

    private void initTitle() {
        formTitle = new FormRow(getActivity()).isTitle(true).setColumns(StyleUtil.getTitles(getActivity())).build();
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
                go2Detail(null);
                break;
            default:
                break;
        }
    }

    private void go2Detail(String data) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), StyleDetailActivity.class);
        if (StringUtil.isNotEmpty(data)) {
            intent.putExtra("data", data);
        }
        startActivityForResult(intent, CodeUtil.STYLEDETAIL);
    }

    private void refreshData() {
        if (formAdapter != null) {
            formDatas.clear();
            styleDatas.clear();
            formAdapter.notifyDataSetChanged();
        }
        getData();
    }

    private void clearFilter() {
        seCode.clear();
        seName.clear();
        bsType.setContext("");
        styleType = "";
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
        List<StyleType> list = new Gson().fromJson(optString, new TypeToken<List<StyleType>>() {
        }.getType());
        if (list == null || list.size() == 0) {
            LoadingFinish(getString(R.string.error_empty));
        } else {
            StyleType styleType = new StyleType();
            styleType.setsClassName(getString(R.string.common_empty));
            styleTypes.add(styleType);
            styleTypes.addAll(list);
            LoadingFinish(null);
            setPickColorType();
        }
    }

    private void setPickColorType() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initPickColorType();
            }
        });
    }

    private void initPickColorType() {
        pvType = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String type = styleTypes.get(options1).getPickerViewText();
                if (!type.equals(styleType)) {
                    styleType = type.equals(getString(R.string.common_empty)) ? "" : styleTypes.get(options1).getPickerViewText();
                    bsType.setContext(styleType);
                }
            }
        })
                .setTitleText("类别选择")
                .setContentTextSize(18)//设置滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .build();
        pvType.setPicker(styleTypes);//一级选择器
        setDialog(pvType);
        pvType.show();
    }

    private List<NetParams> getTypeParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "bscdataclass"));
        params.add(new NetParams("sFields", "sClassName"));
        params.add(new NetParams("sFilters", "sType='mat' and sClassID like '09%'"));
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
}
