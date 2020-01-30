package com.yyy.fuzhuangpad.sale;


import android.app.Dialog;
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

import androidx.fragment.app.Fragment;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.interfaces.OnSelectClickListener;
import com.yyy.fuzhuangpad.util.PxUtil;
import com.yyy.fuzhuangpad.util.StringUtil;
import com.yyy.fuzhuangpad.util.TimeUtil;
import com.yyy.fuzhuangpad.view.button.ButtonSelect;
import com.yyy.fuzhuangpad.view.button.ButtonWithImg;
import com.yyy.fuzhuangpad.view.form.FormColumn;
import com.yyy.fuzhuangpad.view.form.FormRow;
import com.yyy.fuzhuangpad.view.search.SearchEdit;
import com.yyy.yyylibrary.pick.builder.TimePickerBuilder;
import com.yyy.yyylibrary.pick.listener.OnTimeSelectChangeListener;
import com.yyy.yyylibrary.pick.listener.OnTimeSelectListener;
import com.yyy.yyylibrary.pick.view.TimePickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class BillingFragment extends Fragment {
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
    @BindView(R.id.bs_date_start)
    ButtonSelect bsDateStart;
    @BindView(R.id.bs_data_end)
    ButtonSelect bsDataEnd;
    @BindView(R.id.bs_shop)
    ButtonSelect bsShop;

    FormRow formTitle;
    List<FormColumn> titles;
    private TimePickerView pvDateStart;
    private TimePickerView pvDateEnd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_billing, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        initList();
        initTitle();
        initView();
    }

    private void initList() {
        titles = new ArrayList<>();
    }

    private void initTitle() {
        formTitle = new FormRow(getActivity()).isTitle(true).setColumns(BillingUtil.getTitles(getActivity())).build();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getActivity().getResources().getDimensionPixelSize(R.dimen.dp_20));
        params.addRule(RelativeLayout.BELOW, R.id.ll_btn);
        params.topMargin = getActivity().getResources().getDimensionPixelSize(R.dimen.dp_5);
        rlMain.addView(formTitle, params);
    }

    private void initView() {
        initStartDate();
        initEndDate();
    }

    private void initStartDate() {
        bsDateStart.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                pickDateStart();
            }
        });
    }

    private void initEndDate() {
        bsDataEnd.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onClick(View view) {
                pickDateEnd();
            }
        });
    }

    @OnClick({R.id.bwi_remove, R.id.bwi_search, R.id.bs_data_end, R.id.bs_shop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bwi_remove:
                break;
            case R.id.bwi_search:
                break;
            case R.id.bs_date_start:
                break;
            case R.id.bs_data_end:
                break;
            case R.id.bs_shop:
                break;
            default:
                break;
        }
    }

    private void pickDateStart() {
        if (pvDateStart == null) {
            try {
                initPvDateStart();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pvDateStart.show();
    }

    private void pickDateEnd() {
        if (pvDateEnd == null) {
            try {
                initPvDateEnd();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pvDateEnd.show();
    }

    private void initPvDateStart() throws Exception {
        pvDateStart = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                bsDateStart.setContext(StringUtil.getDate(date));
            }
        }).setRangDate(TimeUtil.str2calendar(getString(R.string.common_pickdate_start)), TimeUtil.str2calendar(getString(R.string.common_pickdate_end)))
                .setDate(Calendar.getInstance())
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .setContentTextSize(18).setBgColor(0xFFFFFFFF)
                .build();

        initPvTimeDialog(pvDateStart.getDialog());
        initPvTimeWindow(pvDateStart.getDialog().getWindow());
    }

    private void initPvDateEnd() throws Exception {
        pvDateEnd = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                bsDataEnd.setContext(StringUtil.getDate(date));
            }
        }).setRangDate(TimeUtil.str2calendar(getString(R.string.common_pickdate_start)), TimeUtil.str2calendar(getString(R.string.common_pickdate_end)))
                .setDate(Calendar.getInstance())
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .setContentTextSize(18).setBgColor(0xFFFFFFFF)
                .build();

        initPvTimeDialog(pvDateEnd.getDialog());
        initPvTimeWindow(pvDateEnd.getDialog().getWindow());
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


    private void initPvTimeDialog(Dialog mDialog) {
        if (mDialog != null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    PxUtil.getWidth(getActivity()) / 2,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            params.leftMargin = 0;
            params.rightMargin = 0;
            pvDateStart.getDialogContainerLayout().setLayoutParams(params);
        }
    }
}