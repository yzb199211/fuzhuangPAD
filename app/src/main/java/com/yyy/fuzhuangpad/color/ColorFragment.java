package com.yyy.fuzhuangpad.color;


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

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ColorFragment extends Fragment {
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


    RecyclerView recyclerView;

    private FormRow formTitle;
    private List<FormColumn> titles;
    private List<ColorBeans> colorDatas;
    private List<List<FormColumn>> formDatas;
    private List<ColorType> colorTypes;
    private String url;
    private String address;
    private String companyCode;
    private String colorType = "";
    private String colorId = "";
    private String colorName = "";
    private String filter = "";
    private final int formTitleId = 0x00001000;
    private SharedPreferencesHelper preferencesHelper;
    private FormAdapter colorAdapter;

    private boolean isFrist = true;

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


    private void initList() {
        titles = new ArrayList<>();
        colorDatas = new ArrayList<>();
        formDatas = new ArrayList<>();
        colorTypes = new ArrayList<>();
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
        list.add(new NetParams("sTableName", "vwBscDataColor"));
        list.add(new NetParams("sSorts", "irecno desc"));
        list.add(new NetParams("sFields", "iRecNo,sColorName,sColorID,sClassID,sClassName,sRemark"));
        list.add(new NetParams("sFilters", getFilter()));
        return list;
    }

    private String getFilter() {
        filter = "";
        if (!isFrist) {
            colorId = seCode.getText();
            colorName = seName.getText();
            if (StringUtil.isNotEmpty(colorType)) {
                filter = "sClassName=" + "\'" + colorType + "\'";
            }
            if (StringUtil.isNotEmpty(colorId)) {
                filter = filter + (StringUtil.isNotEmpty(filter) ? " and " : "") + "sColorID like" + "\'|" + colorId + "|\'";
            }
            if (StringUtil.isNotEmpty(colorName)) {
                filter = filter + (StringUtil.isNotEmpty(filter) ? " and " : "") + "sColorName like" + "\'|" + colorName + "|\'";
            }
        } else {
            isFrist = false;
        }
        return filter;
    }

    private void initFormData(String string) throws JSONException {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setListData(jsonObject.optJSONObject("dataset").optString("vwBscDataColor"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setListData(String data) {
        if (StringUtil.isNotEmpty(data)) {
            colorDatas.addAll(new Gson().fromJson(data, new TypeToken<List<ColorBeans>>() {
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
        for (int i = 0; i < colorDatas.size(); i++) {
            ColorBeans color = colorDatas.get(i);
            color.setRow(i);
            formDatas.add(color.getList());
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
        if (colorAdapter == null) {
            initFormAdapter();
        } else {
            colorAdapter.notifyDataSetChanged();
        }
    }

    private void initFormAdapter() {
        colorAdapter = new FormAdapter(formDatas, getActivity());
        colorAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                go2Detail(pos);
            }
        });
        recyclerView.setAdapter(colorAdapter);
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

    private void go2Detail(int pos) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ColorDetailActivity.class);
        intent.putExtra("pos", pos);
        if (pos != -1)
            intent.putExtra("data", new Gson().toJson(colorDatas.get(pos)));
        startActivityForResult(intent, CodeUtil.COLORDETAIL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_color, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        initTitle();
        initView();
    }

    private void initTitle() {
        formTitle = new FormRow(getActivity()).isTitle(true).setColumns(ColorUtil.getTitles(getActivity())).build();
        formTitle.setId(formTitleId);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getActivity().getResources().getDimensionPixelSize(R.dimen.dp_20));
        params.addRule(RelativeLayout.BELOW, R.id.ll_btn);
        params.topMargin = getActivity().getResources().getDimensionPixelSize(R.dimen.dp_5);
        rlMain.addView(formTitle, params);
    }

    private void initView() {
        initColorTypeView();
    }

    private void initColorTypeView() {
        bsType.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (colorTypes.size() == 0) {
                    getColorType();
                } else {
//                    pvColorType.show();
                    popType.showAsDropDown(bsType.getTvContent());
                }
            }
        });
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
        seName.clear();
        bsType.setContext("");
        colorType = "";
        refreshData();
    }

    private void getColorType() {
        LoadingDialog.showDialogForLoading(getActivity());
        new NetUtil(getColorTypeParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initColorData(string);
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


    private List<NetParams> getColorTypeParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "bscdataclass"));
        params.add(new NetParams("sFields", "sClassName"));
        params.add(new NetParams("sFilters", "sType='color'"));
        return params;
    }

    private void initColorData(String string) throws JSONException, Exception {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setColorTypeData(jsonObject.optJSONObject("dataset").optString("bscdataclass"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setColorTypeData(String optString) throws Exception {
        List<ColorType> list = new Gson().fromJson(optString, new TypeToken<List<ColorType>>() {
        }.getType());
        if (list == null || list.size() == 0) {
            LoadingFinish(getString(R.string.error_empty));
        } else {
            ColorType colorType = new ColorType();
            colorType.setsClassName(getString(R.string.common_empty));
            colorTypes.add(colorType);
            colorTypes.addAll(list);
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

    Popwin popType;

    private void initPopType() {
        popType = new Popwin(getActivity(), colorTypes);
        popType.showAsDropDown(bsType.getTvContent());
        popType.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                String type = colorTypes.get(pos).getPickerViewText();
                if (!type.equals(colorType)) {
                    colorType = type.equals(getString(R.string.common_empty)) ? "" : colorTypes.get(pos).getPickerViewText();
                    bsType.setContext(colorType);
//                    refreshData();
                }
            }
        });
    }

    private void refreshData() {
        if (colorAdapter != null) {
            formDatas.clear();
            colorDatas.clear();
            colorAdapter.notifyDataSetChanged();
        }
        getData();
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("code", resultCode + "");
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
//        refreshData();
    }

    private void addData(Intent data) {
        ColorBeans item = new Gson().fromJson(data.getStringExtra("color"), ColorBeans.class);
        colorDatas.add(0, item);
        formDatas.add(0, item.getList());
        refreshList();
    }

    private void removeData(Intent data) {
        int pos = data.getIntExtra("pos", -1);
        if (pos != -1) {
            colorDatas.remove(pos);
            formDatas.remove(pos);
            refreshList();
        }
    }

    private void modifyData(Intent data) {
        int pos = data.getIntExtra("pos", -1);
        ColorBeans item = new Gson().fromJson(data.getStringExtra("color"), ColorBeans.class);
        if (pos != -1) {
            modifyColor(colorDatas.get(pos), item);
            modifyFormData(formDatas.get(pos), item);
        }
    }

    private void modifyFormData(List<FormColumn> row, ColorBeans item) {
        row.get(1).setText(item.getsColorID());
        row.get(2).setText(item.getsColorName());
        row.get(3).setText(item.getsClassName());
        row.get(4).setText(item.getsRemark());
        refreshList();
    }

    private void modifyColor(ColorBeans color, ColorBeans item) {
        color.setsRemark(item.getsRemark())
                .setsColorName(item.getsColorName())
                .setsColorID(item.getsColorID())
                .setsClassID(item.getsClassID())
                .setsClassName(item.getsClassName())
                .setdStopDate(item.getdStopDate());
    }
}
