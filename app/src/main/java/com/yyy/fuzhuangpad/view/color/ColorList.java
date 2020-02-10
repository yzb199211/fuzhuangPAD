package com.yyy.fuzhuangpad.view.color;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.yyy.fuzhuangpad.color.OnColorClick;
import com.yyy.fuzhuangpad.style.StyleColor;

import java.util.ArrayList;
import java.util.List;

public class ColorList extends LinearLayout {
    Context context;
    List<StyleColor> colors = new ArrayList<>();
    List<StyleColor> colorsChecked = new ArrayList<>();

    public ColorList(Context context) {
        this(context, null);
    }

    public ColorList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    public void setData(List<StyleColor> colors) {
        this.colors = colors;
        initData();
    }

    private void initData() {
        String className = "";
        List<List<StyleColor>> groups = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            StyleColor color = colors.get(i);
            if (i == 0) {
                className = color.getsClassName();
                titles.add(className);
                List<StyleColor> colorClass = new ArrayList<>();
                colorClass.add(color);
                groups.add(colorClass);
            } else if (className.equals(color.getsClassName())) {
                groups.get(groups.size() - 1).add(color);
            } else {
                className = color.getsClassName();
                titles.add(className);
                List<StyleColor> colorClass = new ArrayList<>();
                colorClass.add(color);
                groups.add(colorClass);
            }
        }
        for (int j = 0; j < titles.size(); j++) {
            setItem(titles.get(j), groups.get(j));
        }
    }

    private void setItem(String className, List<StyleColor> colorsGroup) {
        ColorItem colorItem = new ColorItem(context);
        colorItem.setData(className, colorsGroup);
        colorItem.setOnColorClick(new OnColorClick() {
            @Override
            public void colorClick(StyleColor color, boolean isChecked) {
                if (isChecked) {
                    colorsChecked.add(color);
                } else {
                    colorsChecked.remove(color);
                }
//                Log.e("checked",new Gson().toJson(colorsChecked));
            }
        });
        addView(colorItem);
    }

    private List<StyleColor> getColors() {
        return colorsChecked;
    }
}
