package com.yyy.fuzhuangpad.sale;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.dialog.LoadingDialog;
import com.yyy.fuzhuangpad.interfaces.OnSelectClickListener;
import com.yyy.fuzhuangpad.interfaces.ResponseListener;
import com.yyy.fuzhuangpad.util.PxUtil;
import com.yyy.fuzhuangpad.util.SharedPreferencesHelper;
import com.yyy.fuzhuangpad.util.StringUtil;
import com.yyy.fuzhuangpad.util.TimeUtil;
import com.yyy.fuzhuangpad.util.Toasts;
import com.yyy.fuzhuangpad.util.net.NetConfig;
import com.yyy.fuzhuangpad.util.net.NetParams;
import com.yyy.fuzhuangpad.util.net.NetUtil;
import com.yyy.fuzhuangpad.view.button.ButtonSelect;
import com.yyy.fuzhuangpad.view.form.FormRow;
import com.yyy.fuzhuangpad.view.search.SearchEdit;
import com.yyy.fuzhuangpad.view.search.SearchText;
import com.yyy.yyylibrary.pick.builder.OptionsPickerBuilder;
import com.yyy.yyylibrary.pick.builder.TimePickerBuilder;
import com.yyy.yyylibrary.pick.listener.OnOptionsSelectListener;
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

public class BillDetailActivity extends AppCompatActivity {
    private final int formTitleId = 0x00001000;
    @BindView(R.id.se_code)
    SearchText seCode;
    @BindView(R.id.bs_shop)
    ButtonSelect bsShop;
    @BindView(R.id.bs_customer)
    ButtonSelect bsCustomer;
    @BindView(R.id.bs_date)
    ButtonSelect bsDate;
    @BindView(R.id.bs_date_delivery)
    ButtonSelect bsDateDelivery;
    @BindView(R.id.bs_sale)
    ButtonSelect bsSale;
    @BindView(R.id.se_remark)
    SearchEdit seRemark;
    @BindView(R.id.st_num)
    SearchText stNum;
    @BindView(R.id.st_total)
    SearchText stTotal;
    @BindView(R.id.ll_main)
    LinearLayout llMain;

    SharedPreferencesHelper preferencesHelper;
    private String url;
    private String address;
    private String companyCode;

    private String shop;
    private int shopId;

    private String customer;
    private int customerId;

    private String saler;
    private String salerId;

    private List<BillShop> shops;
    private List<BillCustomer> customers;
    private List<BillSaler> salers;
    private OptionsPickerView pvShop;
    private OptionsPickerView pvCustomer;
    private OptionsPickerView pvSaler;
    private TimePickerView pvDate;
    private TimePickerView pvDateDelivery;
    FormRow formTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        ButterKnife.bind(this);
        preferencesHelper = new SharedPreferencesHelper(this, getString(R.string.preferenceCache));
        init();
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
        shops = new ArrayList<>();
        customers = new ArrayList<>();
        salers = new ArrayList<>();
    }

    private void initView() {
        initTitle();
        setShopListener();
        setCustomerListener();
        setSalerListener();
        setDateListener();
        setDateDeliveryListener();
    }

    private void initTitle() {
        formTitle = new FormRow(this).isTitle(true).setColumns(BillingUtil.getAddTitles(this)).build();
        formTitle.setId(formTitleId);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.dp_20));
        params.topMargin = getResources().getDimensionPixelSize(R.dimen.dp_5);
        llMain.addView(formTitle, params);
    }

    private void setDateDeliveryListener() {
        bsDateDelivery.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (pvDateDelivery == null) {
                    try {
                        initPvDateDelivery();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    pvDateDelivery.show();
                }
            }
        });
    }

    private void initPvDateDelivery() throws Exception {
        pvDateDelivery = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                bsDateDelivery.setContext(StringUtil.getDate(date));
            }
        }).setRangDate(TimeUtil.str2calendar(getString(R.string.common_pickdate_start)), TimeUtil.str2calendar(getString(R.string.common_pickdate_end)))
                .setDate(Calendar.getInstance())
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .setContentTextSize(18).setBgColor(0xFFFFFFFF)
                .build();
        pvDateDelivery.getDialogContainerLayout().setLayoutParams(initPvTimeDialog());
//        initPvTimeDialog(pvDateEnd.getDialog());
        initPvTimeWindow(pvDateDelivery.getDialog().getWindow());
        pvDateDelivery.show();
    }

    private void setDateListener() {
        bsDate.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (pvDate == null) {
                    try {
                        initPvDate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    pvDate.show();
                }

            }
        });
    }

    private void initPvDate() throws Exception {
        pvDate = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                bsDate.setContext(StringUtil.getDate(date));
            }
        }).setRangDate(TimeUtil.str2calendar(getString(R.string.common_pickdate_start)), TimeUtil.str2calendar(getString(R.string.common_pickdate_end)))
                .setDate(Calendar.getInstance())
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .setContentTextSize(18).setBgColor(0xFFFFFFFF)
                .build();
        pvDate.getDialogContainerLayout().setLayoutParams(initPvTimeDialog());
//        initPvTimeDialog(pvDateEnd.getDialog());
        initPvTimeWindow(pvDate.getDialog().getWindow());
        pvDate.show();
    }

    private void setSalerListener() {
        bsSale.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (shopId == 0) {
                    Toast(getString(R.string.sale_billing_empty_shop));
                    return;
                }
                if (salers.size() == 0) {
                    getSalers();
                } else {
                    pvSaler.show();
                }
            }
        });
    }

    private void getSalers() {
        LoadingDialog.showDialogForLoading(this);
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

    private void initSalerData(String string) throws JSONException, Exception {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setSalerData(jsonObject.optJSONObject("dataset").optString("vwBscDataStockDUser"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setSalerData(String optString) {
        List<BillSaler> list = new Gson().fromJson(optString, new TypeToken<List<BillSaler>>() {
        }.getType());
        if (list == null || list.size() == 0) {
            LoadingFinish(getString(R.string.error_empty));
        } else {
            salers.addAll(list);
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
        pvSaler = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String type = salers.get(options1).getPickerViewText();
                if (!type.equals(saler)) {
                    saler = type.equals(getString(R.string.common_empty)) ? "" : salers.get(options1).getPickerViewText();
                    salerId = salers.get(options1).getsCode();
                    bsSale.setContext(saler);
                }
            }
        })
                .setTitleText("员工选择")
                .setContentTextSize(18)//设置滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .build();
        pvSaler.setPicker(salers);//一级选择器
        setDialog(pvSaler);
        pvSaler.show();
    }

    private List<NetParams> getSalerParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "vwBscDataStockDUser"));
        params.add(new NetParams("sFields", "sCode,sName"));
        params.add(new NetParams("sFilters", "iShopping=1 and iMainrecno=" + shopId));
        return params;
    }

    private void setCustomerListener() {
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


    private void setShopListener() {
        bsShop.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (shops.size() == 0) {
                    getShops();
                } else {
                    pvShop.show();
                }
            }
        });
    }

    private void getCustomers() {
        LoadingDialog.showDialogForLoading(this);
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
            customers.addAll(list);
            LoadingFinish(null);
            setPickCustomer();
        }
    }

    private void setPickCustomer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initPickCustomer();
            }
        });
    }

    private void initPickCustomer() {
        pvCustomer = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
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

    private void getShops() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getShopParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initShopData(string);
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
            shops.addAll(list);
            LoadingFinish(null);
            setPickShop();
        }
    }

    private void setPickShop() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initPickShop();
            }
        });
    }

    private void initPickShop() {
        pvShop = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String type = shops.get(options1).getPickerViewText();
                if (!type.equals(shop)) {
                    shop = type.equals(getString(R.string.common_empty)) ? "" : shops.get(options1).getPickerViewText();
                    shopId = shops.get(options1).getiRecNo();
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

    private List<NetParams> getShopParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "BscdataStockM"));
        params.add(new NetParams("sFields", "iRecNo,sStockName"));
        params.add(new NetParams("sFilters", "iShop=1"));
        return params;
    }

    @OnClick({R.id.bw_exit, R.id.bw_submit, R.id.bw_save, R.id.bwi_add_customer, R.id.bwi_add_style})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bw_exit:
                finish();
                break;
            case R.id.bw_submit:
                break;
            case R.id.bw_save:
                break;
            case R.id.bwi_add_customer:
                go2AddCustomer();
                break;
            case R.id.bwi_add_style:
                go2AddStyle();
                break;
        }
    }


    private void go2AddCustomer() {
        startActivity(new Intent().setClass(this, BillDetailActivity.class));
    }

    private void go2AddStyle() {
        startActivity(new Intent().setClass(this, BillingStyleSelectActivity.class));
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
                PxUtil.getWidth(this) / 2,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        params.leftMargin = 0;
        params.rightMargin = 0;
        return params;
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
                PxUtil.getWidth(this) / 2,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        params.leftMargin = 0;
        params.rightMargin = 0;
        return params;
    }
}
