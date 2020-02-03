package com.yyy.fuzhuangpad.view.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yyy.fuzhuangpad.R;

public class SearchEdit extends LinearLayout {
    Context context;
    String title;
    int titleSize;
    int titleColor;
    int editSize;
    int editColor;

    TextView tvTitle;
    EditText etContent;

    public SearchEdit(Context context) {
        this(context, null);
    }

    public SearchEdit(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(attrs);
        initView();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchEdit);
        title = typedArray.getString(R.styleable.SearchEdit_seTilte);
        titleSize = typedArray.getDimensionPixelSize(R.styleable.SearchEdit_seTitleSize, 12);
        titleColor = typedArray.getColor(R.styleable.SearchEdit_seTitleColor, 0xFF000000);
        editSize = typedArray.getDimensionPixelSize(R.styleable.SearchEdit_seEditSize, 12);
        editColor = typedArray.getColor(R.styleable.SearchEdit_seEditColor, 0xFF000000);
        typedArray.recycle();
    }

    private void initView() {
        initMain();
        initTitle();
        initEdit();
    }

    private void initMain() {
        setGravity(Gravity.CENTER_VERTICAL);
        setOrientation(HORIZONTAL);
        setPadding(5, 0, 5, 0);
    }

    private void initTitle() {
        tvTitle = new TextView(context);
        tvTitle.setText(title);
        tvTitle.setSingleLine();
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        tvTitle.setTextColor(titleColor);
        addView(tvTitle);
    }

    private void initEdit() {
        etContent = new EditText(context);
        etContent.setSingleLine();
        etContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, editSize);
        etContent.setTextColor(editColor);
        etContent.setLayoutParams(etParams());
        etContent.setPadding(5, 0, 5, 0);
//        etContent.setBackground(null);
        etContent.setGravity(Gravity.CENTER_VERTICAL);
        addView(etContent);
    }

    private LinearLayout.LayoutParams etParams() {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        params.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_3);
        return params;
    }

    public String getText() {
        return etContent.getText().toString();
    }
    public void clear(){
        etContent.setText("");
    }
}
