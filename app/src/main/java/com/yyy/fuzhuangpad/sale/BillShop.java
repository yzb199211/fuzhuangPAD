package com.yyy.fuzhuangpad.sale;

import com.yyy.fuzhuangpad.dialog.ISelectText;
import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class BillShop implements IPickerViewData, ISelectText {
    private int iRecNo;
    private String sStockName;

    public int getiRecNo() {
        return iRecNo;
    }

    public void setiRecNo(int iRecNo) {
        this.iRecNo = iRecNo;
    }

    public String getsStockName() {
        return sStockName;
    }

    public void setsStockName(String sStockName) {
        this.sStockName = sStockName;
    }

    @Override
    public String getPickerViewText() {
        return sStockName;
    }

    @Override
    public String getText() {
        return sStockName;
    }
}
