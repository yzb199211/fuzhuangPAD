package com.yyy.fuzhuangpad.sale;

import com.yyy.fuzhuangpad.dialog.ISelectText;
import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class BillStatus implements IPickerViewData, ISelectText {
    private int iStatus;
    private String sStatusName;

    public int getiStatus() {
        return iStatus;
    }

    public void setiStatus(int iStatus) {
        this.iStatus = iStatus;
    }

    public String getsStatusName() {
        return sStatusName;
    }

    public void setsStatusName(String sStatusName) {
        this.sStatusName = sStatusName;
    }

    @Override
    public String getPickerViewText() {
        return sStatusName;
    }

    @Override
    public String getText() {
        return sStatusName;
    }
}
