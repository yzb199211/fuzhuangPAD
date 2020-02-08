package com.yyy.fuzhuangpad.view.color;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yyy.fuzhuangpad.R;
import com.yyy.fuzhuangpad.style.StyleColor;

import java.util.List;

public class ColorItem extends LinearLayout {
    private Context context;
    List<StyleColor> colors;
    String title;
    ColorTitle colorTitle;
    ColorGroup colorGroup;

    public ColorItem(Context context) {
        this(context, null);
    }

    public ColorItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    public void setData(String title, List<StyleColor> colors) {

    }

}
