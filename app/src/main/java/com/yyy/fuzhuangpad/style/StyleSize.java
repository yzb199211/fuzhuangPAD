package com.yyy.fuzhuangpad.style;

import com.yyy.fuzhuangpad.dialog.ISelectText;
import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class StyleSize implements IPickerViewData, ISelectText {
    private String sGroupID;
    private String sGroupName;

    public StyleSize() {
    }

    public StyleSize(String sGroupID, String sGroupName) {
        this.sGroupID = sGroupID;
        this.sGroupName = sGroupName;
    }

    public String getsGroupID() {
        return sGroupID;
    }

    public void setsGroupID(String sGroupID) {
        this.sGroupID = sGroupID;
    }

    public String getsGroupName() {
        return sGroupName;
    }

    public void setsGroupName(String sGroupName) {
        this.sGroupName = sGroupName;
    }

    @Override
    public String getPickerViewText() {
        return sGroupName;
    }

    @Override
    public String getText() {
        return sGroupName;
    }
}
