package com.yyy.fuzhuangpad.sale;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.application.BaseActivity;
import com.yyy.fuzhuangpad.dialog.LoadingDialog;
import com.yyy.fuzhuangpad.interfaces.OnItemClickListener;
import com.yyy.fuzhuangpad.interfaces.ResponseListener;
import com.yyy.fuzhuangpad.util.PxUtil;
import com.yyy.fuzhuangpad.util.SharedPreferencesHelper;
import com.yyy.fuzhuangpad.util.StringUtil;
import com.yyy.fuzhuangpad.util.Toasts;
import com.yyy.fuzhuangpad.util.net.NetConfig;
import com.yyy.fuzhuangpad.util.net.NetParams;
import com.yyy.fuzhuangpad.util.net.NetUtil;
import com.yyy.fuzhuangpad.view.button.ButtonWithImg;
import com.yyy.fuzhuangpad.view.recycle.DividerGridItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.yyy.fuzhuangpad.util.CodeUtil.BILLINGSTYLE;

public class BillingStyleSelectActivity extends BaseActivity {
    SharedPreferencesHelper preferencesHelper;
    @BindView(R.id.rv_style)
    RecyclerView rvStyle;
    @BindView(R.id.bw_delete)
    ButtonWithImg bwDelete;
    @BindView(R.id.bw_save)
    ButtonWithImg bwSave;
    @BindView(R.id.et_search)
    EditText etSearch;

    private String url;
    private String address;
    private String companyCode;
    private String sizeId;
    private List<BillStyle> styles;
    private List<BillStyle> showList;
    private BillStyleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(R.layout.activity_billing_style_select);
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

    private void getData() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initFormData(string);
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

    private void initFormData(String string) throws JSONException {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setListData(jsonObject.optJSONObject("dataset").optString("vwBscDataStyleM"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setListData(String optString) {
        List<BillStyle> list = new Gson().fromJson(optString, new TypeToken<List<BillStyle>>() {
        }.getType());
        if (list == null || list.size() == 0) {
            LoadingFinish(getString(R.string.error_empty));
        } else {
            styles.addAll(list);
            showList.addAll(list);
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
        mAdapter = new BillStyleAdapter(this, showList);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Intent intent = new Intent();
                intent.putExtra("style", new Gson().toJson(styles.get(pos)));
                setResult(BILLINGSTYLE, intent);
                finish();
            }
        });
        rvStyle.setAdapter(mAdapter);
    }

    private List<NetParams> getParams() {
        List<NetParams> list = new ArrayList<>();
        list.add(new NetParams("sCompanyCode", companyCode));
        list.add(new NetParams("otype", "GetTableData"));
        list.add(new NetParams("sTableName", "vwBscDataStyleM"));
        list.add(new NetParams("sFields", "iRecNo,sStyleNo,sStyleName,sClassID,sClassName,fCostPrice,sReMark,sGroupName"));
        list.add(new NetParams("sFilters", "isnull(dStopDate,'2199-01-01')>getdate() and sSizeGroupID=" + sizeId));
        return list;
    }

    private void init() {
        initDefaultData();
        initList();
        initView();
        initSearch();
    }

    private void initSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    modifyShowList(s.toString());
                    refreshList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void modifyShowList(String s) {
        Pattern pattern = Pattern.compile(s);
        showList.clear();
        for (int i = 0; i < styles.size(); i++) {
            Matcher matcher = pattern.matcher(styles.get(i).getsStyleNo());
            Matcher matcher1 = pattern.matcher(styles.get(i).getsStyleName());
            if (matcher.find() || matcher1.find()) {
                showList.add(styles.get(i));
            }
        }
    }


    private void initList() {
        styles = new ArrayList<>();
        showList = new ArrayList<>();
    }

    private void initDefaultData() {
        address = (String) preferencesHelper.getSharedPreference("address", "");
        companyCode = (String) preferencesHelper.getSharedPreference("companyCode", "");
        url = address + NetConfig.server + NetConfig.MobileAppHandler_Method;
        sizeId = getIntent().getStringExtra("sizeId");
    }

    private void initView() {
        rvStyle.setLayoutManager(new GridLayoutManager(this, 6));
        rvStyle.addItemDecoration(new DividerGridItemDecoration(this));
        bwDelete.setVisibility(View.INVISIBLE);
        bwSave.setVisibility(View.INVISIBLE);
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
}
