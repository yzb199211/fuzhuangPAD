package com.yyy.fuzhuangpad.customer;

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
import com.yyy.fuzhuangpad.dialog.LoadingDialog;
import com.yyy.fuzhuangpad.interfaces.OnSelectClickListener;
import com.yyy.fuzhuangpad.interfaces.ResponseListener;
import com.yyy.fuzhuangpad.util.CodeUtil;
import com.yyy.fuzhuangpad.util.PxUtil;
import com.yyy.fuzhuangpad.util.SharedPreferencesHelper;
import com.yyy.fuzhuangpad.util.StringUtil;
import com.yyy.fuzhuangpad.util.TimeUtil;
import com.yyy.fuzhuangpad.util.Toasts;
import com.yyy.fuzhuangpad.util.net.MainQuery;
import com.yyy.fuzhuangpad.util.net.NetConfig;
import com.yyy.fuzhuangpad.util.net.NetParams;
import com.yyy.fuzhuangpad.util.net.NetUtil;
import com.yyy.fuzhuangpad.util.net.Operatortype;
import com.yyy.fuzhuangpad.util.net.Otype;
import com.yyy.fuzhuangpad.view.SelectView;
import com.yyy.fuzhuangpad.view.button.ButtonWithImg;
import com.yyy.fuzhuangpad.view.remark.RemarkEdit;
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

public class CustomerDetailActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.se_code)
    SearchEdit seCode;
    @BindView(R.id.se_name)
    SearchEdit seName;
    @BindView(R.id.sv_type)
    SelectView svType;
    @BindView(R.id.sv_sale)
    SelectView svSale;
    @BindView(R.id.se_contacts)
    SearchEdit seContacts;
    @BindView(R.id.se_phone)
    SearchEdit sePhone;
    @BindView(R.id.sv_dateStop)
    SelectView svDateStop;
    @BindView(R.id.se_address)
    SearchEdit seAddress;
    @BindView(R.id.re_remark)
    RemarkEdit reRemark;
    @BindView(R.id.se_name_short)
    SearchEdit seNameShort;
    @BindView(R.id.bw_delete)
    ButtonWithImg bwDelete;

    CustomerBeans customerBeans;
    SharedPreferencesHelper preferencesHelper;
    private String url;
    private String address;
    private String companyCode;
    private List<CustomerType> customerTypes;
    private List<CustomerSaler> customerSalers;
    private OptionsPickerView pvType;
    private OptionsPickerView pvSale;
    private TimePickerView pvDate;

    String operatortype = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(R.layout.activity_customer_detail);
        ButterKnife.bind(this);
        preferencesHelper = new SharedPreferencesHelper(this, getString(R.string.preferenceCache));
        getData();
        init();
    }

    private void setWindow() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) ((PxUtil.getHeight(this)) * 0.8);
        params.width = (int) ((PxUtil.getWidth(this)) * 0.8f);
        getWindow().setAttributes(params);
    }

    private void getData() {
        String data = getIntent().getStringExtra("data");
        if (StringUtil.isNotEmpty(data)) {
            customerBeans = new Gson().fromJson(data, CustomerBeans.class);
            operatortype = Operatortype.update;
            setData();
        } else {
            customerBeans = new CustomerBeans();
            operatortype = Operatortype.add;
            bwDelete.setVisibility(View.GONE);
        }
    }

    private void setData() {
        seCode.setText(customerBeans.getsCustID());
        seName.setText(customerBeans.getsCustName());
        seNameShort.setText(customerBeans.getsCustShortName());
        svType.setText(customerBeans.getsClassName());
        svSale.setText(customerBeans.getsSaleName());
        seContacts.setText(customerBeans.getsPerson());
        sePhone.setText(customerBeans.getsTel());
        seAddress.setText(customerBeans.getsAddress());
        svDateStop.setText(customerBeans.getdStopDate());
        reRemark.setText(customerBeans.getsRemark());
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
        customerTypes = new ArrayList<>();
        customerSalers = new ArrayList<>();
    }

    private void initView() {
        setTypeListener();
        setSaleListener();
        setDataListener();
    }

    private void setTypeListener() {
        svType.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (customerTypes.size() == 0) {
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

    private List<NetParams> getTypeParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "bscdataclass"));
        params.add(new NetParams("sFields", "sClassName,sClassID"));
        params.add(new NetParams("sFilters", "sType='customer'"));
        return params;
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
            customerTypes.addAll(list);
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
                String type = customerTypes.get(options1).getPickerViewText();
                if (!type.equals(customerBeans.getsClassName())) {
                    customerBeans.setsClassName(type);
                    customerBeans.setsClassID(customerTypes.get(options1).getsClassID());
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
        pvType.setPicker(customerTypes);//一级选择器
        setDialog(pvType);
        pvType.show();
    }

    private void setSaleListener() {
        svSale.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (customerSalers.size() == 0) {
                    getSalerData();
                } else {
                    pvSale.show();
                }
            }
        });
    }

    private void getSalerData() {
        new NetUtil(getSalerParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initSalerData(string);
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

    private List<NetParams> getSalerParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "BscDataPerson"));
        params.add(new NetParams("sFields", "sCode,sName"));
        params.add(new NetParams("sFilters", "1=1"));
        return params;
    }

    private void initSalerData(String string) throws JSONException, Exception {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setSalerData(jsonObject.optJSONObject("dataset").optString("BscDataPerson"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setSalerData(String optString) throws Exception {
        List<CustomerSaler> list = new Gson().fromJson(optString, new TypeToken<List<CustomerSaler>>() {
        }.getType());
        if (list == null || list.size() == 0) {
            LoadingFinish(getString(R.string.error_empty));
        } else {
            customerSalers.add(new CustomerSaler("", getString(R.string.common_empty)));
            customerSalers.addAll(list);
            LoadingFinish(null);
            setPickSaler();
        }
    }

    private void setPickSaler() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initPickSaler();
            }
        });
    }

    private void initPickSaler() {
        pvSale = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String type = customerSalers.get(options1).getPickerViewText().equals(getString(R.string.common_empty)) ? "" : customerSalers.get(options1).getPickerViewText();
                if (!type.equals(customerBeans.getsSaleName())) {
                    customerBeans.setsSaleName(type);
                    customerBeans.setsSaleID(customerSalers.get(options1).getsCode());
                    svSale.setText(type);
                }
            }
        })
                .setTitleText("业务员选择")
                .setContentTextSize(18)//设置滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .build();
        pvSale.setPicker(customerSalers);//一级选择器
        setDialog(pvSale);
        pvSale.show();
    }

    private void setDataListener() {
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

    @OnClick({R.id.bw_exit, R.id.bw_save, R.id.bw_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bw_exit:
                finish();
                break;
            case R.id.bw_save:
                setCustomerBeans();
                save();
                break;
            case R.id.bw_delete:
                delete();
                break;
        }
    }
    private void delete() {
        new NetUtil(deteleParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initDeleteData(string);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingFinish(getString(R.string.error_json));
                }
            }

            @Override
            public void onFail(IOException e) {
                e.printStackTrace();
                LoadingFinish(e.getMessage());
            }
        });
    }

    private void initDeleteData(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        if (jsonObject.optBoolean("success")) {
            LoadingFinish(getString(R.string.success_delete));
            eixt();
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private List<NetParams> deteleParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", Otype.OperateData));
        params.add(new NetParams("mainquery", getDeleltMainquery()));
        return params;
    }

    private String getDeleltMainquery() {
        MainQuery mainQuery = new MainQuery();
        mainQuery.setFields("");
        mainQuery.setFieldsValues("");
        mainQuery.setFieldKeys(customerBeans.paramsFieldKeys());
        mainQuery.setFieldKeysValues(customerBeans.paramsFieldKeysValues());
        mainQuery.setFilterFields(customerBeans.paramsFilterFields());
        mainQuery.setFilterValues(customerBeans.paramsFilterValues());
        mainQuery.setFilterComOprts(customerBeans.paramsFilterComOprts());
        mainQuery.setTableName("BscDataCustomer");
        mainQuery.setOperatortype(Operatortype.delete);
        return new Gson().toJson(mainQuery);
    }
    private void setCustomerBeans() {
        customerBeans.setsCustID(seCode.getText());
        customerBeans.setsCustName(seName.getText());
        customerBeans.setsCustShortName(seNameShort.getText());
        customerBeans.setsPerson(seContacts.getText());
        customerBeans.setdStopDate(svDateStop.getText());
        customerBeans.setsTel(sePhone.getText());
        customerBeans.setsAddress(seAddress.getText());
        customerBeans.setsRemark(reRemark.getText());
    }

    private void save() {
        if (!StringUtil.isNotEmpty(customerBeans.getsCustID())) {
            Toast(getString(R.string.customer_empty_code));
            return;
        }
        if (!StringUtil.isNotEmpty(customerBeans.getsCustName())) {
            Toast(getString(R.string.customer_empty_name));
            return;
        }
        if (!StringUtil.isNotEmpty(customerBeans.getsClassName())) {
            Toast(getString(R.string.customer_empty_type));
            return;
        }
        new NetUtil(saveParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initSaveDate(string);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoadingFinish(getString(R.string.error_json));
                }
            }

            @Override
            public void onFail(IOException e) {
                e.printStackTrace();
                LoadingFinish(e.getMessage());
            }
        });
    }

    private void initSaveDate(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        if (jsonObject.optBoolean("success")) {
            LoadingFinish(getString(R.string.success_save));
            eixt();
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private List<NetParams> saveParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", Otype.OperateData));
        params.add(new NetParams("mainquery", getMainquery()));
        return params;

    }

    private String getMainquery() {
        MainQuery mainQuery = new MainQuery();
        mainQuery.setFields(customerBeans.paramsFields());
        mainQuery.setFieldsValues(customerBeans.paramsFieldsValues());
        mainQuery.setFieldKeys(customerBeans.paramsFieldKeys());
        mainQuery.setFieldKeysValues(customerBeans.paramsFieldKeysValues());
        mainQuery.setFilterFields(customerBeans.paramsFilterFields());
        mainQuery.setFilterValues(customerBeans.paramsFilterValues());
        mainQuery.setFilterComOprts(customerBeans.paramsFilterComOprts());
        mainQuery.setTableName("BscDataCustomer");
        mainQuery.setOperatortype(operatortype);
        return new Gson().toJson(mainQuery);
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
