package com.yyy.fuzhuangpad.sale;

import com.yyy.fuzhuangpad.dialog.ISelectText;
import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class BillCustomer implements IPickerViewData, ISelectText {
    private int irecno;
    private String sCustShortName;

    public int getIrecno() {
        return irecno;
    }

    public void setIrecno(int irecno) {
        this.irecno = irecno;
    }

    public String getsCustShortName() {
        return sCustShortName;
    }

    public void setsCustShortName(String sCustShortName) {
        this.sCustShortName = sCustShortName;
    }

    @Override
    public String getPickerViewText() {
        return sCustShortName;
    }

    @Override
    public String getText() {
        return sCustShortName;
    }
}
