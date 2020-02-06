package com.yyy.fuzhuangpad.style;

import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class StyleType extends StyleBean implements IPickerViewData {
    @Override
    public String getPickerViewText() {
        return getsClassName();
    }
}
