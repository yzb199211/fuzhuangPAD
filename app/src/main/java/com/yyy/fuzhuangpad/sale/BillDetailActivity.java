package com.yyy.fuzhuangpad.sale;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.customer.CustomerDetailActivity;
import com.yyy.fuzhuangpad.dialog.EditDialog;
import com.yyy.fuzhuangpad.dialog.LoadingDialog;
import com.yyy.fuzhuangpad.interfaces.OnDeleteListener;
import com.yyy.fuzhuangpad.interfaces.OnItemClickListener;
import com.yyy.fuzhuangpad.interfaces.OnModifyListener;
import com.yyy.fuzhuangpad.interfaces.OnSelectClickListener;
import com.yyy.fuzhuangpad.interfaces.ResponseListener;
import com.yyy.fuzhuangpad.popwin.Popwin;
import com.yyy.fuzhuangpad.sale.upload.BillChildQuery;
import com.yyy.fuzhuangpad.sale.upload.BillSizeUpload;
import com.yyy.fuzhuangpad.sale.upload.BillStyleUpload;
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
import com.yyy.fuzhuangpad.view.button.ButtonSelect;
import com.yyy.fuzhuangpad.view.button.ButtonWithImg;
import com.yyy.fuzhuangpad.view.form.FormRow;
import com.yyy.fuzhuangpad.view.recycle.RecyclerViewDivider;
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
import java.util.Collections;
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
    @BindView(R.id.bs_class)
    ButtonSelect bsClass;
    @BindView(R.id.bw_delete)
    ButtonWithImg bwDelete;
    SharedPreferencesHelper preferencesHelper;
    private String url;
    private String address;
    private String companyCode;

    private BillBean bill;
    private int iMainRecNo;
    private String shop;
    private int shopId;

    private String customer;
    private int customerId;

    private String saler;
    private String salerId;

    private String classId = "0";
    private String className;


    private List<BillShop> shops;
    private List<BillCustomer> customers;
    private List<BillSaler> salers;
    private List<BillDetailBean> billDetail;
    private List<BillClass> billClass;
    private OptionsPickerView pvShop;
    private OptionsPickerView pvCustomer;
    private OptionsPickerView pvSaler;
    private TimePickerView pvDate;
    private TimePickerView pvDateDelivery;
    private OptionsPickerView pvClass;
    FormRow formTitle;
    RecyclerView recyclerView;
    BillDetailAdapter mAdapter;
    private String operatortype = "";
    private int[] deletekey;
    private int keyPos = 0;
    private int listPos;

    private Popwin popSaler;
    private Popwin popShop;
    private Popwin popClass;

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
        getIntentData();
    }

    private void getIntentData() {

        if (StringUtil.isNotEmpty(getIntent().getStringExtra("data"))) {
            String data = getIntent().getStringExtra("data");
            listPos = getIntent().getIntExtra("pos", -1);
            bill = new Gson().fromJson(data, BillBean.class);
            iMainRecNo = bill.getiRecNo();
            operatortype = Operatortype.update;
            setViewData();
            getData();
        } else {
            bill = new BillBean();
            operatortype = Operatortype.add;
            bwDelete.setVisibility(View.GONE);
            bsDate.setContext(StringUtil.getDate(new Date(System.currentTimeMillis())));
            bill.setdDate(StringUtil.getDate(new Date(System.currentTimeMillis())));
        }
    }

    private void setViewData() {
        classId = bill.getiOrderType() + "";
        className = bill.getsOrderType();
        bsClass.setContext(className);
        seCode.setText(bill.getsOrderNo());
        shop = bill.getsStockName();
        shopId = bill.getiBscdataStockMRecNo();
        bsShop.setContext(shop);
        customer = bill.getsCustShortName();
        customerId = bill.getiBscDataCustomerRecNo();
        bsCustomer.setContext(customer);
        bsDate.setContext(StringUtil.getDate(bill.getdDate(), 2));
        bsDateDelivery.setContext(StringUtil.getDate(bill.getdOrderDate(), 2));
        saler = bill.getsSaleName();
        salerId = bill.getsSaleID();
        bsSale.setContext(saler);
        seRemark.setText(bill.getsRemark());
        stNum.setText(bill.getiQty() + "");
        stTotal.setText(bill.getfTotal() + "");
    }

    private void getData() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initDetailData(string);
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

    private void initDetailData(String string) throws JSONException, Exception {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setDetailData(jsonObject.optJSONObject("dataset").optString("vwSDContractDDPad"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setDetailData(String optString) {
        Log.e("data", optString);
        LoadingFinish(null);
        List<BillDetailBean> list = new Gson().fromJson(optString, new TypeToken<List<BillDetailBean>>() {
        }.getType());
        if (list == null || list.size() == 0) {
//            LoadingFinish(getString(R.string.error_empty));
        } else {
            deletekey = new int[list.size()];
            billDetail.addAll(list);
            refreshList();

        }
    }


    private void refreshList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setTotal();
                Collections.sort(billDetail);
                if (mAdapter == null) {
                    initAdapter();
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void setTotal() {
        int qty = 0;
        double total = 0;
        for (BillDetailBean item : billDetail) {
            qty = qty + item.getiSumQty();
            total = total + item.getfTotal();
        }
        bill.setiQty(qty);
        bill.setfTotal(total);
        stNum.setText(bill.getiQty() + "");
        stTotal.setText(bill.getfTotal() + "");
    }

    EditDialog editDialog;

    private void showEditDialog(int position) {
        if (editDialog == null) {
            editDialog = new EditDialog(this).max(billDetail.get(position).getiSumQty());
        } else {
            editDialog.setMax(billDetail.get(position).getiSumQty());
        }
        editDialog.setOnCloseListener(new EditDialog.OnCloseListener() {
            @Override
            public void onClick(boolean confirm, @NonNull String data) {
                if (confirm) {
                    billDetail.get(position).setiSumQty(Integer.parseInt(data));
                    billDetail.get(position).setfTotal(StringUtil.multiply(billDetail.get(position).getiSumQty(), billDetail.get(position).getfPrice()));
                    refreshList();
                }
            }
        });
        editDialog.show();
    }

    private void initAdapter() {
        mAdapter = new BillDetailAdapter(this, billDetail);
        mAdapter.setOnDeleteListener(new OnDeleteListener() {
            @Override
            public void onDelete(int pos) {
                if (billDetail.get(pos).getiRecNo() != 0) {
                    deletekey[keyPos] = billDetail.get(pos).getiRecNo();
                    keyPos = keyPos + 1;
                }
                billDetail.remove(pos);
                refreshList();
            }
        });
        mAdapter.setOnModifyListener(new OnModifyListener() {
            @Override
            public void onModify(int pos) {
                Log.e("styles", new Gson().toJson(getModifyData(billDetail.get(pos))));
                if (shopId == 0) {
                    Toast(getString(R.string.sale_billing_empty_shop));
                    return;
                }
                startActivityForResult(new Intent()
                                .putExtra("shopId", shopId + "")
                                .putExtra("styles", new Gson().toJson(getModifyData(billDetail.get(pos))))
                                .setClass(BillDetailActivity.this, BillingStyleDetailModifyActivity.class)
                        , CodeUtil.BILLINGSTYLEQTYMODIFY);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private List<BillDetailBean> getModifyData(BillDetailBean billDetailBean) {
        List<BillDetailBean> list = new ArrayList<>();
        boolean isLast = false;
        for (BillDetailBean item : billDetail) {
            if (billDetailBean.getiBscDataStyleMRecNo() == item.getiBscDataStyleMRecNo()) {
                isLast = true;
                list.add(item);
            } else if (isLast)
                break;
        }
        return list;
    }

    private List<NetParams> getParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "vwSDContractDDPad"));
        params.add(new NetParams("sFields", "iRecNo,iMainRecNo,iBscDataStyleMRecNo,iBscDataColorRecNo,sSizeName,iSumQty,fPrice,fTotal,sStyleNo,sColorName,iSerial,sClassName,sStyleName"));
        params.add(new NetParams("sFilters", "iMainRecNo=" + bill.getiRecNo()));
        return params;
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
        billDetail = new ArrayList<>();
        billClass = new ArrayList<>();
    }

    private void initView() {
        initTitle();
        initRecycle();
        setShopListener();
        setCustomerListener();
        setSalerListener();
        setClassListener();
        setDateListener();
        setDateDeliveryListener();
    }


    private void initRecycle() {
        recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayout.VERTICAL));
        recyclerView.setLayoutParams(getRecycleParams());
        llMain.addView(recyclerView);
    }

    private LinearLayout.LayoutParams getRecycleParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f);
        return params;
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
                bill.setdDate(StringUtil.getDate(date));
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
                    popSaler.showAsDropDown(bsSale.getTvContent());
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
//                initPickSaler();
                initPopSaler();
            }
        });
    }

    private void initPopSaler() {
        popSaler = new Popwin(this, salers, bsSale.getTvContent().getWidth());
        popSaler.showAsDropDown(bsSale.getTvContent());
        popSaler.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                String type = salers.get(pos).getPickerViewText();
                if (!type.equals(saler)) {
                    saler = type.equals(getString(R.string.common_empty)) ? "" : type;
                    salerId = salers.get(pos).getsCode();
                    bsSale.setContext(saler);
                }
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
                if (shopId == 0 || billDetail.size() == 0)
                    if (shops.size() == 0) {
                        getShops();
                    } else {
//                        pvShop.show();
                        popShop.showAsDropDown(bsShop.getTvContent());
                    }
            }
        });
    }

    private void setClassListener() {
        bsClass.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (billClass.size() == 0) {
                    getClassData();
                } else {
//                    pvClass.show();
                    popClass.showAsDropDown(bsClass.getTvContent());
                }
            }
        });
    }

    private void getClassData() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getClassParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initClassData(string);
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

    private void initClassData(String string) throws JSONException, Exception {
        Log.e("class", string);
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setClassData(jsonObject.optJSONObject("dataset").optString("bscdatalistd"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setClassData(String optString) {
        List<BillClass> list = new Gson().fromJson(optString, new TypeToken<List<BillClass>>() {
        }.getType());
        if (list == null || list.size() == 0) {
            LoadingFinish(getString(R.string.error_empty));
        } else {
            billClass.addAll(list);
            LoadingFinish(null);
            setPickClass();
        }
    }

    private void setPickClass() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                initPickClass();
                initPopClass();
            }
        });
    }

    private void initPopClass() {
        popClass = new Popwin(this, billClass, bsClass.getTvContent().getWidth());
        popClass.showAsDropDown(bsClass.getTvContent());
        popClass.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                String type = billClass.get(pos).getPickerViewText();
                if (!type.equals(className)) {
                    className = type.equals(getString(R.string.common_empty)) ? "" : type;
                    classId = billClass.get(pos).getsCode();
                    bsClass.setContext(className);

                }
            }
        });
    }

    private void initPickClass() {
        pvClass = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String type = billClass.get(options1).getPickerViewText();
                if (!type.equals(className)) {
                    className = type.equals(getString(R.string.common_empty)) ? "" : billClass.get(options1).getPickerViewText();
                    classId = billClass.get(options1).getsCode();
                    bsClass.setContext(className);

                }
            }
        })
                .setTitleText("类别选择")
                .setContentTextSize(18)//设置滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setLabels("", "", "")
                .isDialog(true)
                .build();
        pvClass.setPicker(billClass);//一级选择器
        setDialog(pvClass);
        pvClass.show();
    }

    private List<NetParams> getClassParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "bscdatalistd"));
        params.add(new NetParams("sFields", "sCode,sName"));
        params.add(new NetParams("sFilters", "sClassId='StyleDWater'"));
        return params;
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
        LoadingFinish(null);
        List<BillCustomer> list = new Gson().fromJson(optString, new TypeToken<List<BillCustomer>>() {
        }.getType());
        if (list == null || list.size() == 0) {
//            LoadingFinish(getString(R.string.error_empty));
        } else {
            customers.addAll(list);
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
        params.add(new NetParams("sFilters", "iCustType=0 and isnull(dStopDate,'2199-01-01')>getdate()"));
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
//                initPickShop();
                initPopShop();
            }
        });
    }

    private void initPopShop() {
        popShop = new Popwin(this, shops, bsShop.getTvContent().getWidth());
        popShop.showAsDropDown(bsShop.getTvContent());
        popShop.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                String type = shops.get(pos).getPickerViewText();
                if (!type.equals(shop)) {
                    shop = type.equals(getString(R.string.common_empty)) ? "" : shops.get(pos).getPickerViewText();
                    shopId = shops.get(pos).getiRecNo();
                    bsShop.setContext(shop);
                }
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

    @OnClick({R.id.bw_exit, R.id.bw_submit, R.id.bw_save, R.id.bwi_add_customer, R.id.bwi_add_style, R.id.bw_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bw_exit:
                finish();
                break;
            case R.id.bw_delete:
                delete();
                break;
            case R.id.bw_submit:
                if (shopId == 0) {
                    Toast(getString(R.string.sale_billing_empty_shop));
                    return;
                }
                if (customerId == 0) {
                    Toast(getString(R.string.sale_billing_empty_customer));
                    return;
                }
                if (TextUtils.isEmpty(bill.getdDate())) {
                    Toast(getString(R.string.sale_billing_empty_date));
                    return;
                }
                if (TextUtils.isEmpty(saler)) {
                    Toast(getString(R.string.sale_billing_empty_saler));
                    return;
                }
                if (TextUtils.isEmpty(className)) {
                    Toast(getString(R.string.sale_billing_empty_class));
                    return;
                }

                if (TextUtils.isEmpty(bill.getsOrderNo())) {
                    getOrderNo(true);
                } else {
                    save(true);
                }
                break;
            case R.id.bw_save:
                if (shopId == 0) {
                    Toast(getString(R.string.sale_billing_empty_shop));
                    return;
                }
                if (customerId == 0) {
                    Toast(getString(R.string.sale_billing_empty_customer));
                    return;
                }
                if (TextUtils.isEmpty(bill.getdDate())) {
                    Toast(getString(R.string.sale_billing_empty_date));
                    return;
                }
                if (TextUtils.isEmpty(saler)) {
                    Toast(getString(R.string.sale_billing_empty_saler));
                    return;
                }
                if (TextUtils.isEmpty(className)) {
                    Toast(getString(R.string.sale_billing_empty_class));
                    return;
                }

                if (TextUtils.isEmpty(bill.getsOrderNo())) {
                    getOrderNo(false);
                } else {
                    save(false);
                }
                break;
            case R.id.bwi_add_customer:
                go2AddCustomer();
                break;
            case R.id.bwi_add_style:
                if (shopId == 0) {
                    Toast(getString(R.string.sale_billing_empty_shop));
                    return;
                }
                go2AddStyle();
                break;
        }
    }

    private void submit(String irecno) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingDialog.showDialogForLoading(BillDetailActivity.this);
            }
        });

        new NetUtil(submitParams(irecno), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initSubmitData(string);
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

    private void initSubmitData(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        if (jsonObject.optBoolean("success")) {
            LoadingFinish(getString(R.string.success_submit));
            bill.setsStatusName(getString(R.string.common_default_status_check));
            eixt(listPos == -1 ? CodeUtil.REFRESH : CodeUtil.MODIFY);
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private List<NetParams> submitParams(String irecno) {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", Otype.MobileSubmit));
        params.add(new NetParams("iFormID", "2002"));
        params.add(new NetParams("userid", (String) preferencesHelper.getSharedPreference("userid", "")));
        params.add(new NetParams("iRecNo", irecno));
        return params;
    }

    private void delete() {
        LoadingDialog.showDialogForLoading(this);
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
            eixt(CodeUtil.DELETE);
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
        mainQuery.setFieldKeys(bill.paramsFieldKeys());
        mainQuery.setFieldKeysValues(bill.paramsFieldKeysValues());
        mainQuery.setFilterFields(bill.paramsFilterFields());
        mainQuery.setFilterValues(bill.paramsFilterValues());
        mainQuery.setFilterComOprts(bill.paramsFilterComOprts());
        mainQuery.setTableName("SdContractM");
        mainQuery.setOperatortype(Operatortype.delete);
        return new Gson().toJson(mainQuery);
    }

    private void getOrderNo(boolean b) {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getOrderParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
//                Log.e("data", string);
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    if (jsonObject.optBoolean("success")) {
                        bill.setsOrderNo(jsonObject.optString("message"));
                        LoadingFinish(null);
                        save(b);
                    } else {
                        LoadingFinish(jsonObject.optString("message"));
                    }
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

    private List<NetParams> getOrderParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", Otype.GetFormBillNo));
        params.add(new NetParams("iFormID", "2002"));
        return params;
    }

    private void save(boolean b) {
        setSaveDate();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingDialog.showDialogForLoading(BillDetailActivity.this);
            }
        });
        new NetUtil(saveParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initSaveDate(string, b);
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

    private void initSaveDate(String data, boolean b) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        if (jsonObject.optBoolean("success") && !b) {
            LoadingFinish(getString(R.string.success_save));
            bill.setsStatusName(getString(R.string.common_default_status));
            bill.setiRecNo(Integer.parseInt(jsonObject.optString("message")));
            eixt(listPos == -1 ? CodeUtil.REFRESH : CodeUtil.MODIFY);
        } else if (jsonObject.optBoolean("success") && b) {
            LoadingFinish(null);
            bill.setiRecNo(Integer.parseInt(jsonObject.optString("message")));
            submit(jsonObject.optString("message"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setSaveDate() {
        bill.setsRemark(seRemark.getText().toString());
        bill.setiBscDataCustomerRecNo(customerId);
        bill.setsCustShortName(customer);
        bill.setdDate(TextUtils.isEmpty(bsDate.getContent()) ? "" : bsDate.getContent());
        bill.setdOrderDate(TextUtils.isEmpty(bsDateDelivery.getContent()) ? "" : bsDateDelivery.getContent());
        bill.setiBscdataStockMRecNo(shopId);
        bill.setsStockName(shop);
        bill.setiOrderType(Integer.parseInt(classId));
        bill.setsOrderType(className);
        bill.setsSaleID(salerId);
        bill.setsSaleName(saler);
    }

    private List<NetParams> saveParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", Otype.OperateData));
        params.add(new NetParams("mainquery", getMainquery()));
        params.add(new NetParams("children", "[" + getChildquery() + "]"));
        return params;
    }

    private String getChildquery() {
        List<BillStyleUpload> styleUploads = new ArrayList<>();
        List<List<BillSizeUpload>> sizeList = new ArrayList<>();
        setUpload(styleUploads, sizeList);

        BillChildQuery mainQueryChild = new BillChildQuery();
        mainQueryChild.setChildtype(BillDetailBean.childtypeParams());
        mainQueryChild.setFieldkey(BillDetailBean.fieldkeyParams());
        mainQueryChild.setLinkfield(BillDetailBean.linkfieldParams());
        mainQueryChild.setTablename(BillDetailBean.tablenameParams());
        mainQueryChild.setData(styleUploads);
        mainQueryChild.setDeleteKey(deletekey);
        mainQueryChild.setGrandsondata(sizeList);
        return new Gson().toJson(mainQueryChild);
    }

    private void setUpload(List<BillStyleUpload> styleUploads, List<List<BillSizeUpload>> billSizeList) {
        BillStyleUpload oldStyle = null;
        for (BillDetailBean item : billDetail) {
            BillStyleUpload style = new BillStyleUpload(item.getiMainRecNo(),
                    item.getiBscDataStyleMRecNo(),
                    item.getiBscDataColorRecNo(),
                    item.getiSumQty(),
                    item.getfPrice(),
                    StringUtil.multiply(item.getiSumQty(), item.getfPrice()));
            BillSizeUpload sizes = new BillSizeUpload(item.getsSizeName(), item.getiSumQty());
            if (!style.equals(oldStyle)) {
                oldStyle = style;
                List<BillSizeUpload> list = new ArrayList<>();
                list.add(sizes);
                billSizeList.add(list);
                styleUploads.add(style);
            } else {
                billSizeList.get(styleUploads.indexOf(oldStyle)).add(sizes);
            }
        }
    }


    private String getMainquery() {
        MainQuery mainQuery = new MainQuery();
        mainQuery.setFields(bill.paramsFields());
        mainQuery.setFieldsValues(bill.paramsFieldsValues((String) preferencesHelper.getSharedPreference("userid", "")));
        mainQuery.setFieldKeys(bill.paramsFieldKeys());
        mainQuery.setFieldKeysValues(bill.paramsFieldKeysValues());
        mainQuery.setFilterFields(bill.paramsFilterFields());
        mainQuery.setFilterValues(bill.paramsFilterValues());
        mainQuery.setFilterComOprts(bill.paramsFilterComOprts());
        mainQuery.setTableName("SdContractM");
        mainQuery.setOperatortype(operatortype);
        return new Gson().toJson(mainQuery);
    }

    private void go2AddCustomer() {
        startActivity(new Intent().setClass(this, CustomerDetailActivity.class));
    }

    private void go2AddStyle() {
        startActivityForResult(new Intent().setClass(this, BillingStyleSelectActivity.class), CodeUtil.BILLINGSTYLE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CodeUtil.BILLINGSTYLE && data != null) {
            go2StyleDetail(data);
        } else if (resultCode == CodeUtil.BILLINGSTYLEQTY && data != null) {
            addStyleQty(data.getStringExtra("style"), data.getStringExtra("styleQty"));
        } else if (resultCode == CodeUtil.BILLINGSTYLEQTYMODIFY && data != null) {
            removeOld(data.getStringExtra("styleOld"));
            addStyleQty(data.getStringExtra("style"), data.getStringExtra("styleQty"));
        }
    }

    private void removeOld(String styleOld) {
        List<BillDetailBean> list = new Gson().fromJson(styleOld, new TypeToken<List<BillDetailBean>>() {
        }.getType());
        billDetail.removeAll(list);
//        refreshList();
    }

    private void addStyleQty(String style, String styleQty) {
        BillStyle item = new Gson().fromJson(style, BillStyle.class);
        List<BillStyleQty> styles = new Gson().fromJson(styleQty, new TypeToken<List<BillStyleQty>>() {
        }.getType());
        if (styles.size() > 0)
            addStyleDetails(switch2StyleDetails(item, styles));
    }

    private void addStyleDetails(List<BillDetailBean> newStyles) {
        for (BillDetailBean item : newStyles) {
            if (billDetail.contains(item)) {
                BillDetailBean oldItem = billDetail.get(billDetail.indexOf(item));
                oldItem.setiSumQty(oldItem.getiSumQty() + item.getiSumQty());
                oldItem.setfTotal(StringUtil.add(oldItem.getfTotal(), item.getfTotal()));
            } else {
                billDetail.add(item);
            }
        }
        refreshList();
    }

    private List<BillDetailBean> switch2StyleDetails(BillStyle item, List<BillStyleQty> styles) {
        List<BillDetailBean> list = new ArrayList<>();
        for (BillStyleQty style : styles) {
            list.add(new BillDetailBean(iMainRecNo, style.getiBscDataStyleMRecNo(),
                    item.getsStyleNo(), style.getiBscDataColorRecNo(),
                    style.getsColorName(), style.getsSizeName(),
                    style.getNum(), item.getfCostPrice(),
                    StringUtil.multiply(style.getNum(), item.getfCostPrice()),
                    "", style.getiSerial(), item.getsClassName(), item.getsStyleName()));
        }
        return list;
    }

    private void go2StyleDetail(Intent data) {
        data.setClass(this, BillingStyleDetailActivity.class);
        data.putExtra("shopId", shopId + "");
        startActivityForResult(data, CodeUtil.BILLINGSTYLEQTY);
    }

    private void eixt(int code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra("pos", listPos);
                intent.putExtra("data", new Gson().toJson(bill));
                setResult(code, intent);
                finish();
            }
        });
    }
}
