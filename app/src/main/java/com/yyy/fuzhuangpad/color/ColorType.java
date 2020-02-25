package com.yyy.fuzhuangpad.color;

import com.yyy.fuzhuangpad.dialog.ISelectText;
import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class ColorType extends ColorBeans implements IPickerViewData, ISelectText {

    @Override
    public String getPickerViewText() {
        return getsClassName();
    }

    @Override
    public String getText() {
        return getsClassName();
    }
}
