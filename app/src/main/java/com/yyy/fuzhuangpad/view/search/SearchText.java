package com.yyy.fuzhuangpad.view.search;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
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

public class SearchText extends LinearLayout {
    Context context;
    String title;
    int titleSize;
    int titleColor;
    int editSize;
    int editColor;
    private int inputType;
    private int maxLength;
    boolean isMust;

    TextView tvTitle;
    TextView tvContent;

    public SearchText(Context context) {
        this(context, null);
    }

    public SearchText(Context context, @Nullable AttributeSet attrs) {
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
        isMust = typedArray.getBoolean(R.styleable.SearchEdit_seMust, false);
        inputType = typedArray.getInt(R.styleable.SearchEdit_seInputType, 0);
        maxLength = typedArray.getInteger(R.styleable.SearchEdit_seMaxLength, 0);
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
        if (isMust) {
            addMust();
        }
        tvTitle = new TextView(context);
        tvTitle.setText(title);
        tvTitle.setSingleLine();
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        tvTitle.setTextColor(titleColor);
        tvTitle.setLayoutParams(titleParams());
        tvTitle.setGravity(Gravity.RIGHT);
        addView(tvTitle);
    }

    private void addMust() {
        TextView textView = new TextView(context);
        textView.setText("*");
        textView.setTextColor(context.getResources().getColor(R.color.red));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        addView(textView);
    }

    private void initEdit() {
        tvContent = new EditText(context);
        tvContent.setSingleLine();
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, editSize);
        tvContent.setTextColor(editColor);
        tvContent.setLayoutParams(etParams());
        tvContent.setPadding(5, 5, 5, 5);
        tvContent.setBackgroundResource(R.drawable.bg_detail_edit);
//        tvContent.setBackground(null);
        tvContent.setGravity(Gravity.CENTER_VERTICAL);
        if (inputType != 0) {
            tvContent.setInputType(getIntype());
        }
        if (maxLength != 0)
            tvContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        addView(tvContent);
    }

    private int getIntype() {
        switch (inputType) {
            case 3:
                return InputType.TYPE_CLASS_NUMBER;
            case 2:
                return InputType.TYPE_NUMBER_FLAG_DECIMAL;
            case 1:
                return InputType.TYPE_TEXT_FLAG_MULTI_LINE;
            default:
                return InputType.TYPE_TEXT_VARIATION_NORMAL;
        }

    }

    private LayoutParams titleParams() {
        LayoutParams params = new LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.sp_10) * 4 + 1, ViewGroup.LayoutParams.WRAP_CONTENT);
        return params;
    }

    private LayoutParams etParams() {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        params.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_3);
        return params;
    }

    public String getText() {
        return tvContent.getText().toString();
    }

    public void setText(String text) {
        tvContent.setText(text);
    }

    public void clear() {
        tvContent.setText("");
    }

}
