package com.yyy.fuzhuangpad.view.color;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.yyy.fuzhuangpad.style.StyleColor;

import java.util.ArrayList;
import java.util.List;

public class ColorItem extends LinearLayout {
    private Context context;
    List<StyleColor> colors;
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
        colors = new ArrayList<>();
        setOrientation(VERTICAL);
        initView();
    }

    private void initView() {
        initTitle();
        initColors();
    }

    private void initTitle() {
        colorTitle = new ColorTitle(context);
        colorTitle.setOnOpenListener(new OnOpenListener() {
            @Override
            public void onOpen(boolean isOpen) {
                if (isOpen) {
                    colorGroup.setVisibility(VISIBLE);
                } else {
                    colorGroup.setVisibility(GONE);
                }
            }
        });
        addView(colorTitle);
    }

    private void initColors() {
        colorGroup = new ColorGroup(context);
        colorGroup.setVisibility(GONE);
        colorGroup.setMarkClickListener(new ColorGroup.MarkClickListener() {
            @Override
            public void clickMark(int position) {

            }
        });
        addView(colorGroup);
    }

    public void setData(String title, List<StyleColor> colors) {
        this.colors.clear();
        this.colors.addAll(colors);
        colorTitle.setTitle(title);
        colorGroup.setData(colors, 12, 20, 5, 20, 5, 20, 20, 20, 20);
    }

}
