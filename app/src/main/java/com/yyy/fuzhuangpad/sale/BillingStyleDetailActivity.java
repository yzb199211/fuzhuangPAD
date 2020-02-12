package com.yyy.fuzhuangpad.sale;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.view.color.ColorGroup;
import com.yyy.fuzhuangpad.view.search.SearchText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillingStyleDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_style_no)
    SearchText tvStyleNo;
    @BindView(R.id.tv_style_class)
    SearchText tvStyleClass;
    @BindView(R.id.tv_style_price)
    SearchText tvStylePrice;
    @BindView(R.id.tv_style_name)
    SearchText tvStyleName;
    @BindView(R.id.tv_style_sizes)
    SearchText tvStyleSizes;
    @BindView(R.id.tv_style_storage)
    SearchText tvStyleStorage;
    @BindView(R.id.cg_color)
    ColorGroup cgColor;
    @BindView(R.id.ll_style)
    LinearLayout llStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_style_detail);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bw_exit, R.id.bw_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bw_exit:
                finish();
                break;
            case R.id.bw_save:
                break;
        }
    }
}
