package com.yyy.fuzhuangpad.color;

import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class ColorType extends ColorBeans implements IPickerViewData {

    @Override
    public String getPickerViewText() {
        return getsClassName();
    }
}
