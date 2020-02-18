package com.yyy.fuzhuangpad.sale;

import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class BillClass implements IPickerViewData {
    private String sCode = "0";
    private String sName = "";

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
}
