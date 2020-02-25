package com.yyy.fuzhuangpad.style;

import com.yyy.fuzhuangpad.dialog.ISelectText;

public class StyleYear implements ISelectText {
    String year;

    public String getYear() {
        return year;
    }

    public StyleYear setYear(String year) {
        this.year = year;
        return this;
    }

    @Override
    public String getText() {
        return year;
    }
}
