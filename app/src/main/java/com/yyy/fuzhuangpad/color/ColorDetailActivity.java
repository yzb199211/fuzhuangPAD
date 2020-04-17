package com.yyy.fuzhuangpad.color;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.application.BaseActivity;
import com.yyy.fuzhuangpad.dialog.JudgeDialog;
import com.yyy.fuzhuangpad.dialog.LoadingDialog;
import com.yyy.fuzhuangpad.interfaces.OnItemClickListener;
import com.yyy.fuzhuangpad.interfaces.OnSelectClickListener;
import com.yyy.fuzhuangpad.interfaces.ResponseListener;
import com.yyy.fuzhuangpad.popwin.Popwin;
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

public class ColorDetailActivity extends BaseActivity {

    @BindView(R.id.se_colorId)
    SearchEdit seColorId;
    @BindView(R.id.se_colorName)
    SearchEdit seColorName;
    @BindView(R.id.sv_colorType)
    SelectView svColorType;
    @BindView(R.id.sv_dateStop)
    SelectView svDateStop;
    @BindView(R.id.re_remark)
    RemarkEdit reRemark;
    @BindView(R.id.bw_delete)
    ButtonWithImg bwDelete;
    ColorBeans colorBeans;
    SharedPreferencesHelper preferencesHelper;
    private String url;
    private String address;
    private String companyCode;
    private List<ColorType> colorTypes;
    private TimePickerView pvDate;
    private Popwin popType;
    //private Popwin popType;
    String operatortype = "";
    int listPos = -1;
    private JudgeDialog judgeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(R.layout.activity_color_detail);
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
        listPos = getIntent().getIntExtra("pos", -1);
        if (StringUtil.isNotEmpty(data)) {
            colorBeans = new Gson().fromJson(data, ColorBeans.class);
            operatortype = Operatortype.update;
            setData();
        } else {
            colorBeans = new ColorBeans();
            operatortype = Operatortype.add;
            bwDelete.setVisibility(View.GONE);
        }
    }

    private void setData() {
        seColorId.setText(colorBeans.getsColorID());
        seColorName.setText(colorBeans.getsColorName());
        svColorType.setText(colorBeans.getsClassName());
        svDateStop.setText(colorBeans.getdStopDate());
        reRemark.setText(colorBeans.getsRemark());

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
        colorTypes = new ArrayList<>();
    }

    private void initView() {
        setTypeListener();
        setDataListener();
    }

    private void setTypeListener() {
        svColorType.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                if (colorTypes.size() == 0) {
                    getColorTypesData();
                } else {
                    popType.showAsDropDown(svColorType.getTvContent());
                }
            }
        });
    }

    private void getColorTypesData() {
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getColorTypeParams(), url, new ResponseListener() {
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

    private List<NetParams> getColorTypeParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", "GetTableData"));
        params.add(new NetParams("sTableName", "bscdataclass"));
        params.add(new NetParams("sFields", "sClassName,sClassID"));
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
            colorTypes.addAll(list);
            LoadingFinish(null);
            setPickColorType();
        }
    }

    private void setPickColorType() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                initPickColorType();
                initPopType();
            }
        });
    }


    private void initPopType() {
        popType = new Popwin(this, colorTypes, svColorType.getTvContent().getWidth());
        popType.showAsDropDown(svColorType.getTvContent());
        popType.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                String type = colorTypes.get(pos).getPickerViewText();
                if (!type.equals(colorBeans.getsClassName())) {
                    colorBeans.setsClassName(type);
                    colorBeans.setsClassID(colorTypes.get(pos).getsClassID());
                    svColorType.setText(type);
                }
            }
        });
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
                setColorBeans();
                save();
                break;
            case R.id.bw_delete:
                jugdeDelete();
                break;
        }
    }

    private void jugdeDelete() {
        if (judgeDialog == null) {
            judgeDialog = new JudgeDialog(this);
            judgeDialog.setContent(getString(R.string.dialog_delete));
            judgeDialog.setOnCloseListener(confirm -> {
                if (confirm) {
                    delete();
                }
            });
        }
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
        mainQuery.setFieldKeys(colorBeans.paramsFieldKeys());
        mainQuery.setFieldKeysValues(colorBeans.paramsFieldKeysValues());
        mainQuery.setFilterFields(colorBeans.paramsFilterFields());
        mainQuery.setFilterValues(colorBeans.paramsFilterValues());
        mainQuery.setFilterComOprts(colorBeans.paramsFilterComOprts());
        mainQuery.setTableName("BscDataColor");
        mainQuery.setOperatortype(Operatortype.delete);
        return new Gson().toJson(mainQuery);
    }

    private void setColorBeans() {
        colorBeans.setdStopDate(svDateStop.getText());
        colorBeans.setsColorID(seColorId.getText());
        colorBeans.setsColorName(seColorName.getText());
        colorBeans.setsRemark(reRemark.getText());
    }

    private void save() {
        if (!StringUtil.isNotEmpty(colorBeans.getsColorName())) {
            Toast(getString(R.string.color_empty_name));
            return;
        }
        if (!StringUtil.isNotEmpty(colorBeans.getsClassName())) {
            Toast(getString(R.string.color_empty_type));
            return;
        }
        LoadingDialog.showDialogForLoading(this);
        new NetUtil(getSaveParams(), url, new ResponseListener() {
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
            colorBeans.setiRecNo(Integer.parseInt(jsonObject.optString("message")));
            eixt(listPos == -1 ? CodeUtil.REFRESH : CodeUtil.MODIFY);
        } else {
            LoadingFinish(jsonObject.optString("message"));
        }
    }

    private List<NetParams> getSaveParams() {
        List<NetParams> params = new ArrayList<>();
        params.add(new NetParams("sCompanyCode", companyCode));
        params.add(new NetParams("otype", Otype.OperateData));
        params.add(new NetParams("mainquery", getMainquery()));
        return params;
    }

    private String getMainquery() {
        MainQuery mainQuery = new MainQuery();
        mainQuery.setFields(colorBeans.paramsFields());
        mainQuery.setFieldsValues(colorBeans.paramsFieldsValues());
        mainQuery.setFieldKeys(colorBeans.paramsFieldKeys());
        mainQuery.setFieldKeysValues(colorBeans.paramsFieldKeysValues());
        mainQuery.setFilterFields(colorBeans.paramsFilterFields());
        mainQuery.setFilterValues(colorBeans.paramsFilterValues());
        mainQuery.setFilterComOprts(colorBeans.paramsFilterComOprts());
        mainQuery.setTableName("BscDataColor");
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
//        window.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
        window.setGravity(Gravity.TOP);//改成Bottom,底部显示
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
        int[] location = new int[2];
        svDateStop.getTvContent().getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                PxUtil.getWidth(this) / 3,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.LEFT);
        params.leftMargin = x - 5;
        params.rightMargin = 0;
        params.topMargin = y + 10;
        return params;
    }

    private void eixt(int code) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent().putExtra("color", new Gson().toJson(colorBeans)).putExtra("pos", listPos);
                setResult(code, intent);
                finish();
            }
        });
    }
}
