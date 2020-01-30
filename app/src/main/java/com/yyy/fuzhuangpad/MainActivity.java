package com.yyy.fuzhuangpad;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.yyy.fuzhuangpad.color.ColorFragment;
import com.yyy.fuzhuangpad.customer.CustomerFragment;
import com.yyy.fuzhuangpad.sale.BillingFragment;
import com.yyy.fuzhuangpad.style.StyleFragment;
import com.yyy.fuzhuangpad.view.main.MainMenu;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.mm_base_customer)
    MainMenu mmBaseCustomer;
    @BindView(R.id.mm_base_style)
    MainMenu mmBaseStyle;
    @BindView(R.id.mm_base_color)
    MainMenu mmBaseColor;
    @BindView(R.id.mm_sale_order)
    MainMenu mmSaleOrder;
    @BindView(R.id.mm_center_pwd)
    MainMenu mmCenterPwd;
    @BindView(R.id.mm_center_exit)
    MainMenu mmCenterExit;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;
    @BindView(R.id.fl_child)
    FrameLayout flChild;

    CustomerFragment customerFragment;
    ColorFragment colorFragment;
    StyleFragment styleFragment;
    BillingFragment billingFragment;

    MainMenu currentMenu;

    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        firstShow();
    }

    @OnClick({R.id.mm_base_customer, R.id.mm_base_style, R.id.mm_base_color, R.id.mm_sale_order, R.id.mm_center_pwd, R.id.mm_center_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mm_base_customer:
                if (customerFragment == null) {
                    customerFragment = new CustomerFragment();
                }
                switchFragment(customerFragment);
                break;
            case R.id.mm_base_style:
                if (styleFragment == null) {
                    styleFragment = new StyleFragment();
                }
                switchFragment(styleFragment);
                break;
            case R.id.mm_base_color:
                if (colorFragment == null) {
                    colorFragment = new ColorFragment();
                }
                switchFragment(colorFragment);
                break;
            case R.id.mm_sale_order:
                if (billingFragment == null) {
                    billingFragment = new BillingFragment();
                }
                switchFragment(billingFragment);
                break;
            case R.id.mm_center_pwd:
                break;
            case R.id.mm_center_exit:
                break;
            default:
                break;
        }
    }

    private void firstShow() {
        customerFragment = new CustomerFragment();
        currentFragment = customerFragment;
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction
                .add(R.id.fl_child, customerFragment)
                .commit();

    }

    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction
                    .hide(currentFragment)
                    .add(R.id.fl_child, targetFragment)
                    .commit();
        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment)
                    .commit();
        }
        currentFragment = targetFragment;
    }
}
