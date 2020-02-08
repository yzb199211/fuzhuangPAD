package com.yyy.fuzhuangpad.view.color;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yyy.fuzhuangpad.R;

public class ColorTitle extends LinearLayout {
    Context context;
    TextView tvOpen;
    TextView tvTitle;

    boolean isOpen = false;
    OnOpenListener onOpenListener;

    public void setOnOpenListener(OnOpenListener onOpenListener) {
        this.onOpenListener = onOpenListener;
    }

    public ColorTitle(Context context) {
        this(context, null);
    }

    public ColorTitle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        setLayoutParams(getParams());
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        setBackgroundColor(context.getResources().getColor(R.color.default_bg_color));
        setTvOpen();
        setTvTitle();
        setPadding(10, 20, 10, 20);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onOpenListener != null)
                    if (isOpen) {
                        isOpen = false;
                        tvOpen.setText(context.getString(R.string.common_close));
                        onOpenListener.onOpen(isOpen);
                    } else {
                        isOpen = true;
                        tvOpen.setText(context.getString(R.string.common_open));
                        onOpenListener.onOpen(isOpen);
                    }
            }
        });
    }

    private void setTvOpen() {
        tvOpen = new TextView(context);
        tvOpen.setText(R.string.common_open);
        tvOpen.setTextColor(context.getResources().getColor(R.color.default_text_color));
        tvOpen.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_10));
        tvOpen.setPadding(10, 0, 10, 0);
        addView(tvOpen);
    }

    private void setTvTitle() {
        tvTitle = new TextView(context);
        tvTitle.setTextColor(context.getResources().getColor(R.color.default_text_color));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_10));
        addView(tvTitle);
    }

    private ViewGroup.LayoutParams getParams() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return params;
    }

    public void setTitle(String title) {

    }
}
