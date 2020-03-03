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
import com.yyy.fuzhuangpad.interfaces.OnEditListener;
import com.yyy.fuzhuangpad.interfaces.OnEditQtyListener;
import com.yyy.fuzhuangpad.view.sale.OnQtyChange;

public class NumSelectView extends LinearLayout {
    Context context;
    TextView tvSubtract;
    TextView tvSubtractTen;
    TextView tvSubtractTwenty;
    TextView tvAdd;
    TextView tvAddTen;
    TextView tvAddTwenty;
    TextView tvNum;
    int num = 0;
    int max = 10000;
    OnQtyChange onQtyChange;
    OnEditListener onEditListener;

    public void setOnEditListener(OnEditListener onEditListener) {
        this.onEditListener = onEditListener;
    }

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
        initSubtractTwenty();
        initSubtractTen();
        initSubtract();
        initNum();
        initAdd();
        initAddTen();
        initAddTwenty();
    }

    private void initAddTwenty() {
        tvAddTwenty = new TextView(context);
        tvAddTwenty.setTextColor(context.getResources().getColor(R.color.default_text_color));
//        tvAddTwenty.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_10));
        tvAddTwenty.setPadding(10, 0, 10, 0);
        tvAddTwenty.setGravity(Gravity.CENTER);
        tvAddTwenty.setText(context.getString(R.string.common_twenty));
        tvAddTwenty.setTextColor(context.getResources().getColor(R.color.red));
        tvAddTwenty.setLayoutParams(getBtnParams());
        tvAddTwenty.setBackgroundColor(context.getResources().getColor(R.color.default_line_color));
        tvAddTwenty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num < max) {
                    num = num + 20;
                    tvNum.setText(num + "");
                    if (onQtyChange != null) {
                        onQtyChange.onQty(num);
                    }
                }
            }
        });
        addView(tvAddTwenty);

    }

    private void initAddTen() {
        tvAddTen = new TextView(context);
        tvAddTen.setTextColor(context.getResources().getColor(R.color.default_text_color));
//        tvAddTen.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_10));
        tvAddTen.setPadding(10, 0, 10, 0);
        tvAddTen.setGravity(Gravity.CENTER);
        tvAddTen.setText(context.getString(R.string.common_ten));
        tvAddTen.setTextColor(context.getResources().getColor(R.color.red));
        tvAddTen.setLayoutParams(getBtnParams());
        tvAddTen.setBackgroundColor(context.getResources().getColor(R.color.default_line_color));
        tvAddTen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num < max) {
                    num = num + 10;
                    tvNum.setText(num + "");
                    if (onQtyChange != null) {
                        onQtyChange.onQty(num);
                    }
                }
            }
        });
        addView(tvAddTen);
    }

    private void initNum() {
        tvNum = new TextView(context);
        tvNum.setTextColor(context.getResources().getColor(R.color.default_text_color));
//        tvNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_10));
        tvNum.setPadding(10, 0, 10, 0);
        tvNum.setGravity(Gravity.CENTER);
        tvNum.setText(num + "");
        tvNum.setLayoutParams(getNumParams());
        tvNum.setBackgroundColor(context.getResources().getColor(R.color.default_bg_color));
        tvNum.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEditListener != null)
                    onEditListener.onEdit(v);
            }
        });
        addView(tvNum);
    }

    private void initAdd() {
        tvAdd = new TextView(context);
        tvAdd.setTextColor(context.getResources().getColor(R.color.default_text_color));
        tvAdd.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_10));
        tvAdd.setPadding(10, 0, 10, 0);
        tvAdd.setGravity(Gravity.CENTER);
        tvAdd.setText(context.getString(R.string.common_close));
        tvAdd.setTextColor(context.getResources().getColor(R.color.red));
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
        tvSubtract.setTextColor(context.getResources().getColor(R.color.common_btn_search));
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

    private void initSubtractTen() {
        tvSubtractTen = new TextView(context);
        tvSubtractTen.setTextColor(context.getResources().getColor(R.color.default_text_color));
//        tvSubtractTen.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_10));
        tvSubtractTen.setPadding(10, 0, 10, 0);
        tvSubtractTen.setGravity(Gravity.CENTER);
        tvSubtractTen.setText(context.getString(R.string.common_ten));
        tvSubtractTen.setTextColor(context.getResources().getColor(R.color.common_btn_search));
        tvSubtractTen.setLayoutParams(getBtnParams());
        tvSubtractTen.setBackgroundColor(context.getResources().getColor(R.color.default_line_color));
        tvSubtractTen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num > 0) {
                    num = (num - 10) < 0 ? 0 : (num - 10);
                    tvNum.setText(num + "");
                    if (onQtyChange != null) {
                        onQtyChange.onQty(num);
                    }
                }
            }
        });
        addView(tvSubtractTen);
    }

    private void initSubtractTwenty() {
        tvSubtractTwenty = new TextView(context);
        tvSubtractTwenty.setTextColor(context.getResources().getColor(R.color.default_text_color));
//        tvSubtractTen.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.sp_10));
        tvSubtractTwenty.setPadding(10, 0, 10, 0);
        tvSubtractTwenty.setGravity(Gravity.CENTER);
        tvSubtractTwenty.setText(context.getString(R.string.common_twenty));
        tvSubtractTwenty.setTextColor(context.getResources().getColor(R.color.common_btn_search));
        tvSubtractTwenty.setLayoutParams(getBtnParams());
        tvSubtractTwenty.setBackgroundColor(context.getResources().getColor(R.color.default_line_color));
        tvSubtractTwenty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (num > 0) {
                    num = (num - 20) < 0 ? 0 : (num - 20);
                    tvNum.setText(num + "");
                    if (onQtyChange != null) {
                        onQtyChange.onQty(num);
                    }
                }
            }
        });
        addView(tvSubtractTwenty);
    }

    private LinearLayout.LayoutParams getBtnParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.dp_20), ViewGroup.LayoutParams.MATCH_PARENT);
        params.leftMargin = 1;
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
