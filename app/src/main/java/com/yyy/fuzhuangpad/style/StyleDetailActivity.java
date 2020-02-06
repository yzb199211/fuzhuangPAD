package com.yyy.fuzhuangpad.style;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.customer.CustomerBeans;
import com.yyy.fuzhuangpad.dialog.LoadingDialog;
import com.yyy.fuzhuangpad.interfaces.OnSelectClickListener;
import com.yyy.fuzhuangpad.interfaces.ResponseListener;
import com.yyy.fuzhuangpad.util.CodeUtil;
import com.yyy.fuzhuangpad.util.PxUtil;
import com.yyy.fuzhuangpad.util.SharedPreferencesHelper;
import com.yyy.fuzhuangpad.util.StringUtil;
import com.yyy.fuzhuangpad.util.TimeUtil;
import com.yyy.fuzhuangpad.util.Toasts;
import com.yyy.fuzhuangpad.util.net.NetConfig;
import com.yyy.fuzhuangpad.util.net.NetParams;
import com.yyy.fuzhuangpad.util.net.NetUtil;
import com.yyy.fuzhuangpad.util.net.Operatortype;
import com.yyy.fuzhuangpad.view.SelectView;
import com.yyy.fuzhuangpad.view.search.SearchEdit;
import com.yyy.yyylibrary.pick.builder.OptionsPickerBuilder;
import com.yyy.yyylibrary.pick.builder.TimePickerBuilder;
import com.yyy.yyylibrary.pick.listener.OnOptionsSelectListener;
import com.yyy.yyylibrary.pick.listener.OnTimeSelectListener;
import com.yyy.yyylibrary.pick.view.BasePickerView;
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

public class StyleDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.se_code)
    SearchEdit seCode;
    @BindView(R.id.se_name)
    SearchEdit seName;
    @BindView(R.id.sv_type)
    SelectView svType;
    @BindView(R.id.sv_size)
    SelectView svSize;
    @BindView(R.id.se_customer)
    SearchEdit seCustomer;
    @BindView(R.id.se_customer_style)
    SearchEdit seCustomerStyle;
    @BindView(R.id.sv_year)
    SelectView svYear;
    @BindView(R.id.se_composition)
    SearchEdit seComposition;
    @BindView(R.id.sv_dateStop)
    SelectView svDateStop;
    @BindView(R.id.se_remark)
    SearchEdit seRemark;
    @BindView(R.id.se_price_trade)
    SearchEdit sePriceTrade;
    @BindView(R.id.se_price_retail)
    SearchEdit sePriceRetail;
    @BindView(R.id.se_price_tag)
    SearchEdit sePriceTag;

    StyleBean styleBean;
    private List<StyleType> styleTypes;
    private List<StyleSize> styleSizes;
    SharedPreferencesHelper preferencesHelper;
    private String url;
    private String address;
    private String companyCode;

    String operatortype = "";
    private OptionsPickerView pvType;
    private OptionsPickerView pvSize;
    private TimePickerView pvDate;
    private TimePickerView pvYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(R.layout.activity_style_detail);
        ButterKnife.bind(this);
        preferencesHelper = new SharedPreferencesHelper(this, getString(R.string.preferenceCache));
        getData();
        init();
    }


    private void setWindow() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) ((PxUtil.getHeight(this)) * 0.8f);
        params.width = (int) ((PxUtil.getWidth(this)) * 0.8f);
        getWindow().setAttributes(params);
    }

    private void getData() {
        String data = getIntent().getStringExtra("data");
        if (StringUtil.isNotEmpty(data)) {
            styleBean = new Gson().fromJson(data, StyleBean.class);
            operatortype = Operatortype.update;
            setData();
        } else {
            styleBean = new StyleBean();
            operatortype = Operatortype.add;
        }
    }

    private void setData() {
        seCode.setText(styleBean.getsStyleNo());
        seName.setText(styleBean.getsStyleName());
        svType.setText(styleBean.getsClassName());
        svSize.setText(styleBean.getsGroupName());
        seCustomer.setText(styleBean.getsCustShortName());
        seCustomerStyle.setText(styleBean.getsCustStyleNo());
        svYear.setText(styleBean.getiYear());
        seComposition.setText(styleBean.getsWaterElents());
        svDateStop.setText(styleBean.getdStopDate());
        seRemark.setText(styleBean.getsReMark());
        sePriceTrade.setText(styleBean.getfBulkTotal1() + "");
        sePriceRetail.setText(styleBean.getfSalePrice() + "");
        sePriceTag.setText(styleBean.getfCostPrice() + "");
    }

    private void init() {
        initDefaultData();
        initList();
        initView();
    }

    private void initDefaultData() {
        address = (String) preferencesHelper.getSharedPreference("address", "");
        companyCode = (String) preferencesHelper.getSharedPreference("companyCode", "");
        url = address + NetConfig.server + NetConfig.MobileAppHandler_Method;
    }

    private void initList() {
        styleSizes = new ArrayList<>();
        styleTypes = new ArrayList<>();
    }

    private void initView() {
        setTypeListener();
        setSizeListener();
        setYearListener();
        setDateListener();
    }

    private void setTypeListener() {
        svType.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (styleTypes.size() == 0) {
                    getTypesData();
                } else {
                    pvType.show();
                }
            }
        });
    }

    private void getTypesData() {
        new NetUtil(getTypeParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initTypeData(string);
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
            styleTypes.addAll(list);
            LoadingFinish(null);
            setPickType();
        }
    }

    private void setPickType() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initPickType();
            }
        });
    }

    private void initPickType() {
        pvType = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String type = styleTypes.get(options1).getPickerViewText();
                if (!type.equals(styleBean.getsClassName())) {
                    styleBean.setsClassName(type);
                    styleBean.setsClassID(styleTypes.get(options1).getsClassID());
                    svType.setText(type);
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
        params.add(new NetParams("sFields", "sClassName,sClassID"));
        params.add(new NetParams("sFilters", "sType='mat' and sClassID like '09%'"));
        return params;
    }

    private void setSizeListener() {
        svSize.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (styleSizes.size() == 0) {
                    getSizeData();
                } else {
                    pvSize.show();
                }
            }
        });
    }

    private void getSizeData() {
        new NetUtil(getSizeParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initSizeData(string);
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

    private void initSizeData(String string) throws JSONException, Exception {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setSizeData(jsonObject.optJSONObject("dataset").optString("BscDataSizeM"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setSizeData(String data) throws Exception {
        List<StyleSize> list = new Gson().fromJson(data, new TypeToken<List<StyleSize>>() {
        }.getType());
        if (list == null || list.size() == 0) {
            LoadingFinish(getString(R.string.error_empty));
        } else {
            styleSizes.add(new StyleSize("", getString(R.string.common_empty)));
            styleSizes.addAll(list);
            LoadingFinish(null);
            setPickSize();
        }
    }

    private void setPickSize() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initPickSize();
            }
        });
    }

    private void initPickSize() {
        pvSize = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String type = styleSizes.get(options1).getPickerViewText().equals(getString(R.string.common_empty)) ? "" : styleSizes.get(options1).getPickerViewText();
                if (!type.equals(styleBean.getsGroupName())) {
                    styleBean.setsGroupName(type);
                    styleBean.setsSizeGroupID(styleSizes.get(options1).getsGroupID());
                    svSize.setText(type);
                }
            }
        })
                .setTitleText("尺码组选择")
                .setContentTextSize(18)//设置滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .build();
        pvSize.setPicker(styleSizes);//一级选择器
        setDialog(pvSize);
        pvSize.show();
    }

    private List<NetParams> getSizeParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "BscDataSizeM"));
        params.add(new NetParams("sFields", "sGroupID,sGroupName"));
        params.add(new NetParams("sFilters", ""));
        return params;
    }

    private void setYearListener() {
        svYear.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (pvYear == null) {
                    try {
                        initPvYear();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast(getString(R.string.error_pcik));
                    }
                }
                pvYear.show();
            }
        });
    }

    private void initPvYear() throws Exception {
        pvYear = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                svYear.setText(StringUtil.getYear(date));
            }
        }).setRangDate(TimeUtil.str2calendar(getString(R.string.common_pickdate_start)), TimeUtil.str2calendar(getString(R.string.common_pickdate_end)))
                .setDate(Calendar.getInstance())
                .setType(new boolean[]{true, false, false, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .setContentTextSize(18)
                .setBgColor(0xFFFFFFFF)
                .setTitleText(getString(R.string.style_detail_year))
                .build();
        setDialog(pvYear);
        initDialogWindow(pvYear.getDialog().getWindow());
    }

    private void setDateListener() {
        svDateStop.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (pvDate == null) {
                    try {
                        initPvDate();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast(getString(R.string.error_pcik));
                    }
                }
                pvDate.show();
            }
        });
    }

    private void initPvDate() throws Exception {
        pvDate = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                svDateStop.setText(StringUtil.getDate(date));
            }
        }).setRangDate(TimeUtil.str2calendar(getString(R.string.common_pickdate_start)), TimeUtil.str2calendar(getString(R.string.common_pickdate_end)))
                .setDate(Calendar.getInstance())
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .setContentTextSize(18)
                .setBgColor(0xFFFFFFFF)
                .setTitleText(getString(R.string.common_date_stop))
                .build();
        setDialog(pvDate);
        initDialogWindow(pvDate.getDialog().getWindow());
    }

    @OnClick({R.id.bw_exit, R.id.bw_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bw_exit:
                finish();
                break;
            case R.id.bw_save:
                break;
        }
    }

    private void LoadingFinish(String msg) {
        runOnUiThread(new Runnable() {
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
        Toasts.showShort(this, msg);
    }

    private void setDialog(BasePickerView pickview) {
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
                PxUtil.getWidth(this) / 2,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        params.leftMargin = 0;
        params.rightMargin = 0;
        return params;
    }

    private void eixt() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setResult(CodeUtil.REFRESH);
                finish();
            }
        });
    }
}
