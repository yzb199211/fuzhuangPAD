package com.yyy.fuzhuangpad.view.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.interfaces.OnSelectClickListener;
import com.yyy.fuzhuangpad.util.StringUtil;

public class ButtonSelect extends LinearLayout {
    private Context context;
    private TextView tvContent;
    private TextView tvTitle;
    private ImageView imageView;
    private RelativeLayout rlSelect;

    private String title;
    private int titleSize;
    private int titleColor;
    private String content="";
    private int contentSize;
    private int contentColor;
    private int src;
    private int srcPadding;
    private boolean isMust;

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
        srcPadding = typedArray.getDimensionPixelOffset(R.styleable.ButtonSelect_bsSrcPadding, 0);
        typedArray.recycle();
    }

    private void initView() {
        initMain();
        initTitle();
        initContent();
        initImg();
        initSelect();
    }

    private void initMain() {
        LayoutInflater.from(context).inflate(R.layout.button_select, this, true);
        setOrientation(HORIZONTAL);
    }

    private void initTitle() {
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(TextUtils.isEmpty(title) ? "" : StringUtil.formatTitle(title));
        tvTitle.setTextColor(titleColor);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        tvTitle.setLayoutParams(titleParams());
        tvTitle.setGravity(Gravity.RIGHT);
        addMust();

    }

    private void addMust() {
        TextView textView = new TextView(context);
        textView.setText("*");
        textView.setTextColor(context.getResources().getColor(R.color.red));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleSize);
        if (!isMust)
            textView.setVisibility(INVISIBLE);
        addView(textView,0);
    }

    private LayoutParams titleParams() {
        LayoutParams params = new LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.sp_10) * 4 + 1, ViewGroup.LayoutParams.WRAP_CONTENT);
        return params;
    }

    private void initContent() {
        tvContent = findViewById(R.id.tv_content);
        tvContent.setText(content);
        tvContent.setTextColor(contentColor);
        tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentSize);
    }

    private void initImg() {
        imageView = findViewById(R.id.image);
        imageView.setPadding(srcPadding, srcPadding, srcPadding, srcPadding);
        imageView.setLayoutParams(initImageParams());
        if (src != 0)
            imageView.setImageResource(src);
    }

    private void initSelect() {
        rlSelect = findViewById(R.id.rl_content);
        rlSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSelectClickListener != null)
                    onSelectClickListener.onClick(v);
            }
        });
    }

    private RelativeLayout.LayoutParams initImageParams() {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(titleSize, titleSize);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dp_3);
//        LayoutParams params = new LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.sp_14), context.getResources().getDimensionPixelSize(R.dimen.sp_14));
        return params;
    }

    public void setContext(String content) {
        this.content = content;
        if (tvContent != null)
            tvContent.setText(content);
    }

    public String getContent() {
        return content;
    }

    public TextView getTvContent() {
        return tvContent;
    }
}
