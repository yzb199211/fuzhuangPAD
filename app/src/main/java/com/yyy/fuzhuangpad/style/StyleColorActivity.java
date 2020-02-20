package com.yyy.fuzhuangpad.style;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.dialog.LoadingDialog;
import com.yyy.fuzhuangpad.interfaces.ResponseListener;
import com.yyy.fuzhuangpad.util.CodeUtil;
import com.yyy.fuzhuangpad.util.PxUtil;
import com.yyy.fuzhuangpad.util.SharedPreferencesHelper;
import com.yyy.fuzhuangpad.util.StringUtil;
import com.yyy.fuzhuangpad.util.Toasts;
import com.yyy.fuzhuangpad.util.net.NetConfig;
import com.yyy.fuzhuangpad.util.net.NetParams;
import com.yyy.fuzhuangpad.util.net.NetUtil;
import com.yyy.fuzhuangpad.view.button.ButtonWithImg;
import com.yyy.fuzhuangpad.view.color.ColorGroup;
import com.yyy.fuzhuangpad.view.color.ColorItem;
import com.yyy.fuzhuangpad.view.color.ColorList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StyleColorActivity extends AppCompatActivity {

    @BindView(R.id.ll_color)
    LinearLayout llColor;
    @BindView(R.id.scroll)
    ScrollView scrollView;
    @BindView(R.id.bw_delete)
    ButtonWithImg bwDelete;

    ColorList colorList;

    List<StyleColor> colors;
    List<StyleColor> colorsChecked;
    List<StyleColor> colorsOld;
    SharedPreferencesHelper preferencesHelper;
    private String url;
    private String address;
    private String companyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(R.layout.activity_style_color);
        ButterKnife.bind(this);
        preferencesHelper = new SharedPreferencesHelper(this, getString(R.string.preferenceCache));
        init();
        getData();
    }

    private void init() {
        bwDelete.setVisibility(View.INVISIBLE);
        initList();
        initIntentData();
        initDefaultData();
    }

    private void initList() {
        colors = new ArrayList<>();
        colorsChecked = new ArrayList<>();
        colorsOld = new ArrayList<>();
    }

    private void initIntentData() {
        colorsOld = new Gson().fromJson(getIntent().getStringExtra("colors"), new TypeToken<List<StyleColor>>() {
        }.getType());
    }

    private void initDefaultData() {
        address = (String) preferencesHelper.getSharedPreference("address", "");
        companyCode = (String) preferencesHelper.getSharedPreference("companyCode", "");
        url = address + NetConfig.server + NetConfig.MobileAppHandler_Method;
    }


    private void getData() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getParams(), url, new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initColorData(string);
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

    private void initColorData(String string) throws JSONException {
        JSONObject jsonObject = new JSONObject(string);
        if (jsonObject.optBoolean("success")) {
            setListData(jsonObject.optJSONObject("dataset").optString("vwBscDataColor"));
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private void setListData(String data) {
        if (StringUtil.isNotEmpty(data)) {
            colors.addAll(new Gson().fromJson(data, new TypeToken<List<StyleColor>>() {
            }.getType()));
            setColorView();
        } else {
            LoadingFinish(getString(R.string.error_empty));
        }
    }

    private void setColorView() {
        LoadingFinish(null);
        if (colors.size() > 0) {
            setColorChecked();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    colorList = new ColorList(StyleColorActivity.this);
                    colorList.setData(colors, colorsChecked);
                    llColor.addView(colorList);
                }
            });
        }
    }

    private void setColorChecked() {
        if (colorsOld.size() > 0) {
            for (StyleColor color : colorsOld) {
                if (color.isChecked()) {
                    setChecked(color.getiRecNo());
                }
            }
        }
    }

    private void setChecked(int iRecNo) {
        for (int i = 0; i < colors.size(); i++) {
            if (iRecNo == colors.get(i).getiRecNo()) {
                colors.get(i).setChecked(true);
                colorsChecked.add(colors.get(i));
            }
        }
    }

    private List<NetParams> getParams() {
        List<NetParams> list = new ArrayList<>();
        list.add(new NetParams("sCompanyCode", companyCode));
        list.add(new NetParams("otype", "GetTableData"));
        list.add(new NetParams("sTableName", "vwBscDataColor"));
        list.add(new NetParams("sFields", "iRecNo,sColorName,sColorID,sClassID,sClassName"));
        list.add(new NetParams("sFilters", ""));
        list.add(new NetParams("sSorts", "sClassID asc"));
        return list;
    }

    private void setWindow() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) ((PxUtil.getHeight(this)) * 0.8);
        params.width = (int) ((PxUtil.getWidth(this)) * 0.8f);
        getWindow().setAttributes(params);
    }

    @OnClick({R.id.bw_exit, R.id.bw_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bw_exit:
                finish();
                break;
            case R.id.bw_save:
                if (colorList != null) {
                    colorList.getColors();
                    setResult(CodeUtil.STYLECOLOR, new Intent().putExtra("colors", new Gson().toJson(colorList.getColors())));
                    finish();
                }
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
