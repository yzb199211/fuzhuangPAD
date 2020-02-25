package com.yyy.fuzhuangpad.sale;

import com.yyy.fuzhuangpad.dialog.ISelectText;
import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class BillSaler implements IPickerViewData , ISelectText {
    private String sCode;
    private String sName;

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
