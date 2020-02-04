package com.yyy.fuzhuangpad.view.remark;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.application.BaseApplication;

public class RemarkEdit extends LinearLayout implements View.OnKeyListener {
    Context context;
    String title;
    int titleSize;
    int titleColor;
    int editSize;
    int editColor;

    TextView tvTitle;
    EditText etContent;

    public RemarkEdit(Context context) {
        this(context, null);
    }

    public RemarkEdit(Context context, @Nullable AttributeSet attrs) {
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
        etContent.setLines(5);
        etContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, editSize);
        etContent.setTextColor(editColor);
        etContent.setLayoutParams(etParams());
        etContent.setGravity(Gravity.START);
        etContent.setPadding(5, 5, 5, 5);
        etContent.setBackground(context.getResources().getDrawable(R.drawable.bg_remark_edit));
        etContent.setOnKeyListener(this);
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

    public void setText(String text) {
        etContent.setText(text);
    }

    public void clear() {
        etContent.setText("");
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((keyCode == EditorInfo.IME_ACTION_SEND
                || keyCode == EditorInfo.IME_ACTION_DONE || keyCode == KeyEvent.KEYCODE_ENTER) && event.getAction() == KeyEvent.ACTION_DOWN)
            closeKeybord();
        return true;
    }

    public void closeKeybord() {
        InputMethodManager imm = (InputMethodManager) ((Activity) context).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(), 0);
        }
    }

}
