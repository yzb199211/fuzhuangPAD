package com.yyy.fuzhuangpad.style;

import com.yyy.fuzhuangpad.dialog.ISelectText;
import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class StyleType extends StyleBean implements IPickerViewData, ISelectText {
    @Override
    public String getPickerViewText() {
        return getsClassName();
    }

    @Override
    public String getText() {
        return getsClassName();
    }
}
