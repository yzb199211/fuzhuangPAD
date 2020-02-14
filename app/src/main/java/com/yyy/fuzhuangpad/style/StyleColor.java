package com.yyy.fuzhuangpad.style;

import com.yyy.fuzhuangpad.color.ColorBeans;

public class StyleColor extends ColorBeans {
    private boolean isChecked = false;
    private int iBscDataColorRecNo;

    public int getiBscDataColorRecNo() {
        return iBscDataColorRecNo;
    }

    public void setiBscDataColorRecNo(int iBscDataColorRecNo) {
        this.iBscDataColorRecNo = iBscDataColorRecNo;
    }

    public StyleColor() {
    }

    public StyleColor(String colorID, String colorName) {
        setsColorID(colorID);
        setsColorName(colorName);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
