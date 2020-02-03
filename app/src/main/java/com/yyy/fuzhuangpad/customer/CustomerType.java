package com.yyy.fuzhuangpad.customer;

import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class CustomerType extends CustomerBeans implements IPickerViewData {
    @Override
    public String getPickerViewText() {
        return getsClassName();
    }
}
