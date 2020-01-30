package com.yyy.fuzhuangpad.view.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yyy.fuzhuangpad.R;

public class MainMenu extends LinearLayout {
    Context context;
    private String text;
    private int imgSrc;
    private int textColor;
    private int textSize;
    private TextView tvContent;
    private ImageView ivImg;

    public void setText(String text) {
        this.text = text;
    }

    public void setImgSrc(int imgSrc) {
        this.imgSrc = imgSrc;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public String getText() {
        return text;
    }

    public MainMenu(Context context) {
        this(context, null);
    }

    public MainMenu(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(attrs);
        initView();
    }


    private void initAttrs(AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MainMenu);
        text = array.getString(R.styleable.MainMenu_text);
        imgSrc = array.getResourceId(R.styleable.MainMenu_imgSrc, 0);
        textColor = array.getColor(R.styleable.MainMenu_tvolor, 0);
        textSize = array.getDimensionPixelSize(R.styleable.MainMenu_tvSize, 100);
        array.recycle();
    }

    private void initView() {
        initLayout();
        initImageView();
        initTextView();
    }

    private void initLayout() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setPadding(0, 0, 0, 1);
        setBackgroundColor(context.getResources().getColor(R.color.white));
    }


    private void initImageView() {
        ivImg = new ImageView(context);
        ivImg.setLayoutParams(ivParams());
        if (imgSrc != 0)
            ivImg.setImageResource(imgSrc);
        addView(ivImg);
    }

    private void initTextView() {
        tvContent = new TextView(context);
        tvContent.setText(text);
        tvContent.setTextColor(textColor);
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tvContent.setGravity(Gravity.CENTER_VERTICAL);
        tvContent.setLayoutParams(tvParams());
        tvContent.setSingleLine();
        addView(tvContent);
    }


    private LinearLayout.LayoutParams ivParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.dp_15), ViewGroup.LayoutParams.MATCH_PARENT);
        return params;
    }

    private LinearLayout.LayoutParams tvParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = context.getResources().getDimensionPixelOffset(R.dimen.dp_3);
        return params;
    }
}
