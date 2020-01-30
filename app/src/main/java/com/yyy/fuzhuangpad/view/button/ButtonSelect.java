package com.yyy.fuzhuangpad.view.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.interfaces.OnSelectClickListener;

public class ButtonSelect extends RelativeLayout {
    private Context context;
    private TextView tvContent;
    private TextView tvTitle;
    private ImageView imageView;

    private String title;
    private int titleSize;
    private int titleColor;
    private String content;
    private int contentSize;
    private int contentColor;
    private int src;

    OnSelectClickListener onSelectClickListener;

    public OnSelectClickListener getOnSelectClickListener() {
        return onSelectClickListener;
    }

    public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener) {
        this.onSelectClickListener = onSelectClickListener;
    }

    public ButtonSelect(Context context) {
        this(context, null);
    }

    public ButtonSelect(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        initAttrs(attrs);
        initView();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ButtonSelect);
        title = typedArray.getString(R.styleable.ButtonSelect_bsTitle);
        titleSize = typedArray.getDimensionPixelSize(R.styleable.ButtonSelect_bsTitleSize, 12);
        titleColor = typedArray.getColor(R.styleable.ButtonSelect_bsTitleColor, 0xFF000000);
        content = typedArray.getString(R.styleable.ButtonSelect_bsContent);
        contentSize = typedArray.getDimensionPixelSize(R.styleable.ButtonSelect_bsContentSize, 12);
        contentColor = typedArray.getColor(R.styleable.ButtonSelect_bsContentColor, 0xFF000000);
        src = typedArray.getResourceId(R.styleable.ButtonSelect_bsSrc, 0);
        typedArray.recycle();
    }

    private void initView() {
        initMain();
        initTitle();
        initContent();
        initImg();
    }


    private void initMain() {
        setGravity(Gravity.CENTER_VERTICAL);
    }

    private void initTitle() {

    }

    private void initContent() {
    }

    private void initImg() {
    }

    private LayoutParams titleParams() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return params;
    }

    private LayoutParams contentParams() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return params;
    }

    private LayoutParams imgParams() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return params;
    }
}
