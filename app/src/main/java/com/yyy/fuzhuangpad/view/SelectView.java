package com.yyy.fuzhuangpad.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.interfaces.OnSelectClickListener;

public class SelectView extends LinearLayout {
    private Context context;
    private static final int contentId = 0x00001001;
    String title;
    int titleSize;
    int titleColor;
    int selectSize;
    int setectColor;
    boolean isMust;

    TextView tvTitle;
    TextView tvContent;
    ImageView iv;
    RelativeLayout rl;
    OnSelectClickListener onSelectClickListener;

    public void setOnSelectClickListener(OnSelectClickListener onSelectClickListener) {
        this.onSelectClickListener = onSelectClickListener;
    }

    public SelectView(Context context) {
        this(context, null);
    }

    public SelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(attrs);
        initView();
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectView);
        title = typedArray.getString(R.styleable.SelectView_svTilte);
        titleSize = typedArray.getDimensionPixelSize(R.styleable.SelectView_svTitleSize, 12);
        titleColor = typedArray.getColor(R.styleable.SelectView_svTitleColor, 0xFF000000);
        selectSize = typedArray.getDimensionPixelSize(R.styleable.SelectView_svContentSize, 12);
        setectColor = typedArray.getColor(R.styleable.SelectView_svContentColor, 0xFF000000);
        isMust = typedArray.getBoolean(R.styleable.SelectView_svMust, false);
        typedArray.recycle();
    }

    private void initView() {
        initMain();
        initTitle();
        initRl();
        initContent();
        initImage();
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
        addView(tvTitle);
    }

    private void addMust() {
        TextView textView = new TextView(context);
        textView.setText("*");
        textView.setTextColor(context.getResources().getColor(R.color.red));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        addView(textView);
    }

    private void initRl() {
        rl = new RelativeLayout(context);
        rl.setLayoutParams(rlParams());
        rl.setBackgroundResource(R.drawable.bg_detail_edit);
        rl.setPadding(5, 5, 5, 5);
        rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSelectClickListener != null)
                    onSelectClickListener.onClick(v);
            }
        });
        addView(rl);
    }

    private LinearLayout.LayoutParams rlParams() {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        params.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_3);
        return params;
    }

    private void initContent() {
        tvContent = new TextView(context);
        tvContent.setLayoutParams(contentParams());
        tvContent.setTextColor(setectColor);
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, selectSize);
        tvContent.setSingleLine();
        tvContent.setBackground(null);
        tvContent.setId(contentId);
        rl.addView(tvContent);
    }

    private RelativeLayout.LayoutParams contentParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        return params;
    }

    private void initImage() {
        iv = new ImageView(context);
        iv.setImageResource(R.mipmap.bg_select);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        iv.setLayoutParams(ivParams());
        rl.addView(iv);
    }

    private RelativeLayout.LayoutParams ivParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(20, 20);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.ALIGN_BOTTOM, contentId);
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
