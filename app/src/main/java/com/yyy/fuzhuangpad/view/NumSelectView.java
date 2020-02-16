package com.yyy.fuzhuangpad.view;

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
import com.yyy.fuzhuangpad.view.sale.OnQtyChange;

public class NumSelectView extends LinearLayout {
    Context context;
    TextView tvSubtract;
    TextView tvAdd;
    TextView tvNum;
    int num = 0;
    int max = 10000;
    OnQtyChange onQtyChange;

    public void setOnQtyChange(OnQtyChange onQtyChange) {
        this.onQtyChange = onQtyChange;
    }

    public NumSelectView(Context context) {
        this(context, null);
    }

    public NumSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        initMain();
        initBtn();
    }

    private void initBtn() {
        initSubtract();
        initNum();
        initAdd();
    }

    private void initNum() {
        tvNum = new TextView(context);
        tvNum.setTextColor(context.getResources().getColor(R.color.default_text_color));
        tvNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_10));
        tvNum.setPadding(10, 0, 10, 0);
        tvNum.setGravity(Gravity.CENTER);
        tvNum.setText(num + "");
        tvNum.setLayoutParams(getNumParams());
        tvNum.setBackgroundColor(context.getResources().getColor(R.color.default_bg_color));
        addView(tvNum);
    }

    private void initAdd() {
        tvAdd = new TextView(context);
        tvAdd.setTextColor(context.getResources().getColor(R.color.default_text_color));
        tvAdd.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_10));
        tvAdd.setPadding(10, 0, 10, 0);
        tvAdd.setGravity(Gravity.CENTER);
        tvAdd.setText(context.getString(R.string.common_close));
        tvAdd.setLayoutParams(getBtnParams());
        tvAdd.setBackgroundColor(context.getResources().getColor(R.color.default_line_color));
        tvAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num < max) {
                    num = num + 1;
                    tvNum.setText(num + "");
                    if (onQtyChange != null) {
                        onQtyChange.onQty(num);
                    }
                }
            }
        });
        addView(tvAdd);
    }

    private void initSubtract() {
        tvSubtract = new TextView(context);
        tvSubtract.setTextColor(context.getResources().getColor(R.color.default_text_color));
        tvSubtract.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_10));
        tvSubtract.setPadding(10, 0, 10, 0);
        tvSubtract.setGravity(Gravity.CENTER);
        tvSubtract.setText(context.getString(R.string.common_open));
        tvSubtract.setLayoutParams(getBtnParams());
        tvSubtract.setBackgroundColor(context.getResources().getColor(R.color.default_line_color));
        tvSubtract.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num > 0) {
                    num = num - 1;
                    tvNum.setText(num + "");
                    if (onQtyChange != null) {
                        onQtyChange.onQty(num);
                    }
                }
            }
        });
        addView(tvSubtract);
    }

    private ViewGroup.LayoutParams getBtnParams() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.dp_20), ViewGroup.LayoutParams.MATCH_PARENT);
        return params;
    }

    private LinearLayout.LayoutParams getNumParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        return params;
    }

    private void initMain() {
        setOrientation(HORIZONTAL);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        tvNum.setText(num + "");
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
