package com.yyy.fuzhuangpad.view.search;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
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
import com.yyy.fuzhuangpad.util.StringUtil;

public class SearchEdit extends LinearLayout implements View.OnKeyListener {
    private Context context;
    private String title;
    private int titleSize;
    private int titleColor;
    private int editSize;
    private int editColor;
    private int inputType;
    private int maxLength;
    private boolean isMust;

    private TextView tvTitle;
    private EditText etContent;
    private TextView tvContent;

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
        addMust();
        tvTitle = new TextView(context);
        tvTitle.setText(TextUtils.isEmpty(title) ? "" : StringUtil.formatTitle(title));
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
        if (!isMust)
            textView.setVisibility(INVISIBLE);
        addView(textView);
    }

    private void initEdit() {
        etContent = new EditText(context);
        etContent.setSingleLine();
        etContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, editSize);
        etContent.setTextColor(editColor);
        etContent.setLayoutParams(etParams());
        etContent.setPadding(5, 5, 5, 5);
        etContent.setBackgroundResource(R.drawable.bg_detail_edit);
//        etContent.setBackground(null);
        etContent.setGravity(Gravity.CENTER_VERTICAL);
        etContent.setOnKeyListener(this);
        if (inputType != 0) {
            etContent.setInputType(getIntype());
        }
        if (maxLength != 0)
            etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        addView(etContent);
    }

    private int getIntype() {
        switch (inputType) {
            case 3:
                return InputType.TYPE_CLASS_NUMBER;
            case 2:
                return EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL;
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
        if (tvContent!=null){
            tvContent.setText(text);
        }
    }

    public void clear() {
        etContent.setText("");
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
        InputMethodManager imm = (InputMethodManager) ((Activity) context).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void forbirdEdit() {
        etContent.setVisibility(GONE);
        tvContent = new TextView(context);
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
}
