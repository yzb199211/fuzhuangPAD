package com.yyy.fuzhuangpad.style;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.util.PxUtil;
import com.yyy.fuzhuangpad.view.color.ColorGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StyleColorActivity extends AppCompatActivity {

    @BindView(R.id.ll_color)
    LinearLayout llColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(R.layout.activity_style_color);
        ButterKnife.bind(this);
        ColorGroup group = new ColorGroup(this);
        List<StyleColor> list = new ArrayList<>();
        list.add(new StyleColor("红色", "红色"));
        list.add(new StyleColor("绿色", "绿色"));
        list.add(new StyleColor("蓝", "蓝"));
        list.add(new StyleColor("白色", "白色"));
        list.add(new StyleColor("白色", "白色"));
        list.add(new StyleColor("白色", "白色"));
        list.add(new StyleColor("白色", "白色"));
        list.add(new StyleColor("白色", "白色11111111"));
        list.add(new StyleColor("白色", "白色"));
        list.add(new StyleColor("白色", "白色1111"));
        list.add(new StyleColor("白色", "白色"));
        list.add(new StyleColor("白色", "白色"));
        list.add(new StyleColor("白色", "白色11111"));
        list.add(new StyleColor("白色", "白色"));
        list.add(new StyleColor("白色", "白色"));
        group.setData(list, 12, 20, 5, 20, 5, 20, 20, 20, 20);
        llColor.addView(group);
    }

    private void setWindow() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) ((PxUtil.getHeight(this)) * 0.8);
        params.width = (int) ((PxUtil.getWidth(this)) * 0.8f);
        getWindow().setAttributes(params);
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
