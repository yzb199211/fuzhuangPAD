package com.yyy.fuzhuangpad.view.sale;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.view.NumSelectView;

public class SaleStyleQtyView extends LinearLayout {
    Context context;
    TextView tvColor;
    TextView tvSize;
    TextView tvStorage;
    NumSelectView nsvQty;

    public SaleStyleQtyView(Context context) {
        this(context, null);
    }

    public SaleStyleQtyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.item_sale_style_qty, this, true);
        init();
    }

    private void init() {
        initView();
        initMain();
    }


    private void initView() {
        tvColor = findViewById(R.id.tv_color);
        tvSize = findViewById(R.id.tv_size);
        tvStorage = findViewById(R.id.tv_storage);
        nsvQty = findViewById(R.id.nsv_qty);
    }

    private void initMain() {
        setOrientation(HORIZONTAL);
        setLayoutParams(getParams());
    }

    private ViewGroup.LayoutParams getParams() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, context.getResources().getDimensionPixelSize(R.dimen.dp_20));
        return params;
    }

    public void setData(SaleStyleQty item) {
        tvColor.setText(item.getsColorName());
        tvSize.setText(item.getsSizeName());
        tvStorage.setText(item.getiQty() + "");
        nsvQty.setMax(item.getiQty());
    }
}