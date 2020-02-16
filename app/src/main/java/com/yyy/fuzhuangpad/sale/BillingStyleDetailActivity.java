package com.yyy.fuzhuangpad.sale;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.dialog.LoadingDialog;
import com.yyy.fuzhuangpad.interfaces.ResponseListener;
import com.yyy.fuzhuangpad.style.StyleColor;
import com.yyy.fuzhuangpad.util.CodeUtil;
import com.yyy.fuzhuangpad.util.ImageLoaderUtil;
import com.yyy.fuzhuangpad.util.PxUtil;
import com.yyy.fuzhuangpad.util.SharedPreferencesHelper;
import com.yyy.fuzhuangpad.util.StringUtil;
import com.yyy.fuzhuangpad.util.Toasts;
import com.yyy.fuzhuangpad.util.net.NetConfig;
import com.yyy.fuzhuangpad.util.net.NetParams;
import com.yyy.fuzhuangpad.util.net.NetUtil;
import com.yyy.fuzhuangpad.view.button.ButtonWithImg;
import com.yyy.fuzhuangpad.view.color.ColorGroup;
import com.yyy.fuzhuangpad.view.recycle.RecyclerViewDivider;
import com.yyy.fuzhuangpad.view.search.SearchText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillingStyleDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_style_no)
    SearchText tvStyleNo;
    @BindView(R.id.tv_style_class)
    SearchText tvStyleClass;
    @BindView(R.id.tv_style_price)
    SearchText tvStylePrice;
    @BindView(R.id.tv_style_name)
    SearchText tvStyleName;
    @BindView(R.id.tv_style_sizes)
    SearchText tvStyleSizes;
    @BindView(R.id.tv_style_storage)
    SearchText tvStyleStorage;
    @BindView(R.id.cg_color)
    ColorGroup cgColor;
    @BindView(R.id.bw_delete)
    ButtonWithImg bwDelete;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private String url;
    private String address;
    private String companyCode;
    private String styleId;
    private String shopId;
    private String style;
    SharedPreferencesHelper preferencesHelper;

    List<BillColor> colors;
    List<BillStyleQty> styleQty;
    BillStyleQtyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(R.layout.activity_billing_style_detail);
        ButterKnife.bind(this);
        preferencesHelper = new SharedPreferencesHelper(this, getString(R.string.preferenceCache));
        init();
        getData();
    }

    private void setWindow() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) ((PxUtil.getHeight(this)) * 0.8f);
        params.width = (int) ((PxUtil.getWidth(this)) * 0.8f);
        getWindow().setAttributes(params);
    }

    private void init() {
        getIntentData();
        initDefaultData();
        initList();
        initView();
    }

    private void initView() {
        bwDelete.setVisibility(View.INVISIBLE);
        ImageLoaderUtil.loadDrawableImg(ivLogo, R.mipmap.default_style);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayout.VERTICAL));
    }

    private void initList() {
        colors = new ArrayList<>();
        styleQty = new ArrayList<>();
    }

    private void initDefaultData() {
        address = (String) preferencesHelper.getSharedPreference("address", "");
        companyCode = (String) preferencesHelper.getSharedPreference("companyCode", "");
        url = address + NetConfig.server + NetConfig.MobileAppHandler_Method;
    }

    private void getIntentData() {
//        Log.e("tiem", getIntent().getStringExtra("style"));
        shopId = getIntent().getStringExtra("shopId");
        this.style = getIntent().getStringExtra("style");
        BillStyle style = new Gson().fromJson(this.style, BillStyle.class);
        styleId = style.getiRecNo() + "";
        tvStyleNo.setText(style.getsStyleNo());
        tvStyleName.setText(style.getsStyleName());
        tvStyleClass.setText(style.getsClassName());
        tvStyleSizes.setText(style.getsGroupName());
        tvStylePrice.setText(style.getfCostPrice() + "");
        tvStyleStorage.setText("0");
    }

    private void getData() {
//        getStorageData();
        getColorData();
    }

    private void getColorData() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getColorParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
//                Log.e("color", string);
                try {
                    initColorData(string);
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

    private void initColorData(String string) throws JSONException {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setColorData(jsonObject.optJSONObject("dataset").optString("vwBscDataStyleDColor"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setColorData(String optString) {
        Log.e("color", optString);
        List<BillColor> list = new Gson().fromJson(optString, new TypeToken<List<BillColor>>() {
        }.getType());
        if (list == null || list.size() == 0) {
            LoadingFinish(getString(R.string.error_empty));
        } else {
            colors.addAll(list);
            LoadingFinish(null);
            setColorGroup();

        }
    }

    private void setColorGroup() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                List<StyleColor> list = new ArrayList<>();
//                for (StyleColor color : colors) {
//                    list.add(color);
//                }
                cgColor.setData(colors, getResources().getDimensionPixelSize(R.dimen.sp_10), 20, 5, 20, 5, 20, 20, 20, 20);
                cgColor.setMarkClickListener(new ColorGroup.MarkClickListener() {
                    @Override
                    public void clickMark(int position, boolean isChecked) {
                        if (isChecked) {
                            if (colors.get(position).getStyleQty().size() == 0) {
                                getStyleQty(position);
                            } else {
                                styleQty.addAll(colors.get(position).getStyleQty());
                                refreshList();
                            }
                        } else {
                            styleQty.removeAll(colors.get(position).getStyleQty());
                            refreshList();
                        }
                    }
                });
            }
        });
    }

    private List<NetParams> getColorParams() {
        List<NetParams> list = new ArrayList<>();
        list.add(new NetParams("sCompanyCode", companyCode));
        list.add(new NetParams("otype", "GetTableData"));
        list.add(new NetParams("sTableName", "vwBscDataStyleDColor"));
        list.add(new NetParams("sFields", "iRecNo,sColorName,iBscDataColorRecNo"));
        list.add(new NetParams("sFilters", "iMainRecNo=" + styleId));
//        Log.e("fileter", "iMainRecNo=" + styleId);
        return list;
    }

    private void getStyleQty(int pos) {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getStyleQtyParams(colors.get(pos).getiBscDataColorRecNo()), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
//                Log.e("style", string);
                try {
                    initStyleQty(string, pos);
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

    private void initStyleQty(String string, int pos) throws JSONException {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setStyleQty(jsonObject.optJSONObject("dataset").optString("vwProductSizeStock"), pos);
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setStyleQty(String optString, int pos) {
//        Log.e("style", optString);
        List<BillStyleQty> list = new Gson().fromJson(optString, new TypeToken<List<BillStyleQty>>() {
        }.getType());
        if (list == null || list.size() == 0) {
            LoadingFinish(getString(R.string.error_empty));
        } else {
            colors.get(pos).setStyleQty(list);
            styleQty.addAll(list);
            refreshList();
            LoadingFinish(null);
        }
    }

    private void refreshList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAdapter == null) {
                    initAdapter();
                } else {
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initAdapter() {
        mAdapter = new BillStyleQtyAdapter(this, styleQty);
        recyclerView.setAdapter(mAdapter);
    }

    private List<NetParams> getStyleQtyParams(int colorId) {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "vwProductSizeStock"));
        params.add(new NetParams("sFields", "sColorName,sSizeName,iQty,iBscDataStyleMRecNo,iBscDataColorRecNo"));
        params.add(new NetParams("sFilters", "iBscDataStyleMRecNo=" + styleId + " and iBscDataColorRecNo=" + colorId + " and iBscdataStockMRecNo=" + shopId));
        return params;
    }

    @OnClick({R.id.bw_exit, R.id.bw_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bw_exit:
                finish();
                break;
            case R.id.bw_save:
                save();
                break;
        }
    }

    private void save() {
        List<BillStyleQty> list = getSaveStyle();
        Log.e("size", list.size() + "");
        if (list.size() == 0) {
            finish();
        } else {
            Intent intent = new Intent();
            intent.putExtra("style", style);
            intent.putExtra("styleQty", new Gson().toJson(list));
            setResult(CodeUtil.BILLINGSTYLEQTY, intent);
            finish();
        }
    }

    private List<BillStyleQty> getSaveStyle() {
//        Log.e("styles", new Gson().toJson(styleQty));
        List<BillStyleQty> list = new ArrayList<>();
        for (BillStyleQty billStyleQty : styleQty) {
            if (billStyleQty.getNum() > 0) {
                list.add(billStyleQty);
            }
        }
        return list;
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
}
