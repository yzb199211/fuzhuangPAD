package com.yyy.fuzhuangpad.sale;

import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class BillShop implements IPickerViewData {
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
}
