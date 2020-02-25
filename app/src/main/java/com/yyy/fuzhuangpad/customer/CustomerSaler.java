package com.yyy.fuzhuangpad.customer;

import com.yyy.fuzhuangpad.dialog.ISelectText;
import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class CustomerSaler implements IPickerViewData, ISelectText {
    private String sCode;
    private String sName;

    public CustomerSaler(String sCode, String sName) {
        this.sCode = sCode;
        this.sName = sName;
    }

    public CustomerSaler() {
    }

    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    @Override
    public String getPickerViewText() {
        return sName;
    }

    @Override
    public String getText() {
        return sName;
    }
}
