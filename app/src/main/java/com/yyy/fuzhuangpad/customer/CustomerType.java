package com.yyy.fuzhuangpad.customer;

import com.yyy.fuzhuangpad.dialog.ISelectText;
import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class CustomerType extends CustomerBeans implements IPickerViewData, ISelectText {
    @Override
    public String getPickerViewText() {
        return getsClassName();
    }

    @Override
    public String getText() {
        return getsClassName();
    }
}
