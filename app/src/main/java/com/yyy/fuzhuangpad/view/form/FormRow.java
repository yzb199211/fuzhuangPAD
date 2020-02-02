package com.yyy.fuzhuangpad.view.form;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;

import com.yyy.fuzhuangpad.R;

import java.util.List;

public class FormRow extends LinearLayout {
    private Context context;
    private boolean isTitle;
    private int gravity;
    @ColorRes
    private int textColor;

    private List<FormColumn> columns;


    public FormRow(Context context) {
        this(context, null);
    }

    public FormRow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    private void init() {
        initMain();
        initRow();
    }


    private void initMain() {
        if (isTitle) {
            setTextColor(R.color.white);
            setBackgroundColor(context.getResources().getColor(R.color.form_bg_title));
            gravity = Gravity.CENTER;
        } else {
            setTextColor(R.color.default_content_color);
            setBackgroundColor(context.getResources().getColor(R.color.white));
            gravity = Gravity.CENTER_VERTICAL;
        }
    }

    private void initRow() {
        if (columns != null && columns.size() > 0) {
            for (FormColumn column : columns) {
                addView(getColumn(column));
            }
        }
    }

    private TextView getColumn(FormColumn column) {
        TextView textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_8));
        textView.setTextColor(context.getResources().getColor(textColor));
        textView.setGravity(column.isCenter() ? Gravity.CENTER : gravity);
        textView.setLayoutParams(columnParams(column.getWeight()));
        textView.setText(column.getText());
        return textView;
    }

    private LayoutParams columnParams(float weight) {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, weight);
        return params;
    }

    public void setTextColor(@ColorRes int textColor) {
        this.textColor = textColor;
    }


    public FormRow setColumns(List<FormColumn> columns) {
        this.columns = columns;
        return this;
    }

    public FormRow isTitle(boolean isTitle) {
        this.isTitle = isTitle;
        return this;
    }

    public FormRow addDefaultParams() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(R.dimen.dp_20));
        setLayoutParams(params);
        return this;
    }

    public FormRow build() {
        if (this.columns != null) {
            init();
        }
        return this;
    }
}
