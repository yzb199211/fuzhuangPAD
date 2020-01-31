package com.yyy.fuzhuangpad.view.radio;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.yyy.fuzhuangpad.R;

import java.util.ArrayList;
import java.util.List;

public class EnableOrUnable extends LinearLayout {
    Context context;
    List<String> items;

    RadioButton selectedBtn;
    OnCheckedChangeLinstener onCheckedChangeLinstener;


    public EnableOrUnable(Context context) {
        super(context, null);
    }

    public EnableOrUnable(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        initMain();
        initList();
        initView();
    }

    private void initMain() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initList() {
        items = new ArrayList<>();
        items.add(context.getString(R.string.common_enable));
        items.add(context.getString(R.string.common_unable));
    }

    private void initView() {
        for (String item : items) {
            addView(getItem(item));
        }
    }

    private RadioButton getItem(String item) {
        RadioButton view = new RadioButton(context);
        view.setText(item);
        view.setLayoutParams(getParams());
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!view.isChecked() &&onCheckedChangeLinstener!=null){

                }
            }
        });
        return view;
    }

    private LayoutParams getParams() {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        return params;
    }

    public void setOnCheckedChangeLinstener(OnCheckedChangeLinstener onCheckedChangeLinstener) {
        this.onCheckedChangeLinstener = onCheckedChangeLinstener;
    }
}
