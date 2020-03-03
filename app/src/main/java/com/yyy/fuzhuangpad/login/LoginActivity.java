package com.yyy.fuzhuangpad.login;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.google.gson.Gson;

import com.yyy.fuzhuangpad.BuildConfig;
import com.yyy.fuzhuangpad.MainActivity;
import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.application.BaseActivity;
import com.yyy.fuzhuangpad.application.BaseApplication;
import com.yyy.fuzhuangpad.dialog.JudgeDialog;
import com.yyy.fuzhuangpad.dialog.LoadingDialog;
import com.yyy.fuzhuangpad.interfaces.PermissionListener;
import com.yyy.fuzhuangpad.interfaces.ResponseListener;
import com.yyy.fuzhuangpad.login.model.LoginBean;
import com.yyy.fuzhuangpad.util.FileUtil;
import com.yyy.fuzhuangpad.util.SharedPreferencesHelper;
import com.yyy.fuzhuangpad.util.StringUtil;
import com.yyy.fuzhuangpad.util.Toasts;
import com.yyy.fuzhuangpad.util.VersionUtil;
import com.yyy.fuzhuangpad.util.net.NetConfig;
import com.yyy.fuzhuangpad.util.net.NetParams;
import com.yyy.fuzhuangpad.util.net.NetUtil;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;

public class LoginActivity extends BaseActivity implements View.OnKeyListener {
    private final String TAG = "LoginActivity";
    @BindView(R.id.et_user)
    EditText etUser;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.cb_remember_pwd)
    CheckBox checkRememberPwd;

    private static final int REQUEST_CODE_SCAN = 11;
    SharedPreferencesHelper preferencesHelper;

    String userid;
    String password;
    String url;
    String address;
    String companyCode;

    boolean isTest;
    boolean isRemember = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        ButterKnife.bind(this);
        preferencesHelper = new SharedPreferencesHelper(this, getString(R.string.preferenceCache));
        inti();
        initView();
    }

    private void inti() {
        isTest = true;
        address = (String) preferencesHelper.getSharedPreference("address", "");
        if (isTest == true) {
            preferencesHelper.put("address", NetConfig.address);
            preferencesHelper.put("companyCode", NetConfig.companyCode);
            address = NetConfig.address;
        }
        companyCode = (String) preferencesHelper.getSharedPreference("companyCode", "");
        isRemember = (boolean) preferencesHelper.getSharedPreference("remember", false);
    }

    private void initView() {
        String userId = (String) preferencesHelper.getSharedPreference("userid", "");
        String passWord = (String) preferencesHelper.getSharedPreference("password", "");
        etUser.setText(userId);
        etPwd.setText(passWord);
        etUser.setOnKeyListener(this);
        etPwd.setOnKeyListener(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initRemember();
    }

    private void initRemember() {

        checkRememberPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRemember = isChecked;
                if (isChecked == false) {
                    etPwd.setText("");
                    preferencesHelper.put("password", "");
                }
            }
        });
        checkRememberPwd.setChecked(isRemember);
    }


    @OnClick({R.id.btn_login, R.id.tv_sweep, R.id.iv_sweep})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_login:
                try {
                    isNone();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_sweep:
                permission();
                break;
            case R.id.iv_sweep:
                permission();
                break;
        }
    }


    /**
     * 判断url,用户名和密码是否为空
     */
    private void isNone() throws Exception {
        if (TextUtils.isEmpty(address)) {
            Toasts.showShort(this, getString(R.string.login_url_empty));
            return;
        }

        url = address + NetConfig.server + NetConfig.Login_Method;
        userid = etUser.getText().toString();
        password = etPwd.getText().toString();

        if (TextUtils.isEmpty(url)) {
            Toasts.showShort(LoginActivity.this, "请扫描二维码");
            return;
        }
        if (TextUtils.isEmpty(userid)) {
            Toasts.showShort(LoginActivity.this, "请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toasts.showShort(LoginActivity.this, "请输入密码");
            return;
        }
        getContact();
    }

    /**
     * 获取数据
     */
    private void getContact() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getParams(), url, new OkHttpClient(), new ResponseListener() {
            @Override
            public void onSuccess(String string) {
                try {
                    initData2(string);
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toasts.showLong(LoginActivity.this, "登录失败");
                            LoadingDialog.cancelDialogForLoading();
                        }
                    });
                }
            }

            @Override
            public void onFail(final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoadingDialog.cancelDialogForLoading();
                        Toasts.showLong(LoginActivity.this, e.getMessage());
                    }
                });

            }
        });
    }

    /**
     * 设置参数
     *
     * @return
     */
    private List<NetParams> getParams() {
        List<NetParams> list = new ArrayList<>();
        list.add(new NetParams("userid", userid));
        list.add(new NetParams("password", password));
        list.add(new NetParams("companycode", companyCode));
        return list;
    }

    JudgeDialog updateDialog;

    private void initData2(String response) throws Exception {
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.optBoolean("success")) {
            final Intent intent = new Intent()
                    .setClass(LoginActivity.this, MainActivity.class);
            preferencesHelper.put("userid", etUser.getText().toString());
            preferencesHelper.put("remember", isRemember);
            if (isRemember)
                preferencesHelper.put("password", etPwd.getText().toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoadingDialog.cancelDialogForLoading();
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoadingDialog.cancelDialogForLoading();
                    Toasts.showLong(LoginActivity.this, jsonObject.optString("message"));
                }
            });

        }
    }

    /**
     * 扫描权限申请和扫描逻辑处理
     */
    private void permission() {
        requestRunPermisssion(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
            @Override
            public void onGranted() {
                Intent intent = new Intent().setClass(LoginActivity.this, CaptureActivity.class);
                /* ZxingConfig是配置类
                 * 可以设置是否显示底部布局，闪光灯，相册，
                 * 是否播放提示音  震动
                 * 设置扫描框颜色等
                 * 也可以不传这个参数
                 * */
                ZxingConfig config = new ZxingConfig();
                config.setDecodeBarCode(false);//是否扫描条形码 默认为true
                config.setFullScreenScan(false);//是否全屏扫描  默认为true  设为false则只会在扫描框中扫描
                intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
//                Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_LONG).show();
                Toasts.showShort(LoginActivity.this, "授权失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码返回值
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                try {
                    storageAddress(content);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toasts.showShort(this, getString(R.string.error_json));
                }
                Log.e(TAG, content);
            } else {
                Toasts.showShort(this, getString(R.string.error_empty));
            }
        }
    }

    /*扫描数据处理*/
    private void storageAddress(String content) throws JSONException {
        JSONObject jsonObject = new JSONObject(content);
        String address = jsonObject.optString("ServerAddr");
        String addressImg = jsonObject.optString("ServerImageAddr");
        if (StringUtil.isNotEmpty(address)) {
            preferencesHelper.put("address", address);
            this.address = address;
        } else {
            Toasts.showShort(this, getString(R.string.login_address_empty));
        }

        if (StringUtil.isNotEmpty(addressImg)) {
            preferencesHelper.put("addressImg", address);
        } else {
            Toasts.showShort(this, getString(R.string.error_empty_address));
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (updateDialog != null && updateDialog.isShowing()) {
            updateDialog.dismiss();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((keyCode == EditorInfo.IME_ACTION_SEND
                || keyCode == EditorInfo.IME_ACTION_DONE || keyCode == KeyEvent.KEYCODE_ENTER) && event.getAction() == KeyEvent.ACTION_DOWN) {
            closeKeybord();
            return true;
        }
        return false;
    }

    public void closeKeybord() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
