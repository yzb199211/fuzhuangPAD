package com.yyy.fuzhuangpad.color;

import android.text.TextUtils;
import android.util.Log;

import com.yyy.fuzhuangpad.util.StringUtil;
import com.yyy.fuzhuangpad.view.form.FormColumn;

import java.util.ArrayList;
import java.util.List;

public class ColorBeans {
    private int iRecNo;
    private String sColorID = "";
    private String sColorName = "";
    private String sClassID = "";
    private String sClassName = "";
    private String dStopDate = "";
    private String sRemark = "";
    private int row;

    public void setRow(int row) {
        this.row = row;
    }


    public int getiRecNo() {
        return iRecNo;
    }

    public ColorBeans setiRecNo(int iRecNo) {
        this.iRecNo = iRecNo;
        return this;
    }

    public String getsColorID() {
        return TextUtils.isEmpty(sColorID) ? "" : sColorID;
    }

    public ColorBeans setsColorID(String sColorID) {
        this.sColorID = sColorID;
        return this;
    }

    public String getsColorName() {
        return TextUtils.isEmpty(sColorName) ? "" : sColorName;
    }

    public ColorBeans setsColorName(String sColorName) {
        this.sColorName = sColorName;
        return this;
    }

    public String getsClassID() {
        return TextUtils.isEmpty(sClassID) ? "" : sClassID;
    }

    public ColorBeans setsClassID(String sClassID) {
        this.sClassID = sClassID;
        return this;
    }

    public String getsClassName() {
        return TextUtils.isEmpty(sClassName) ? "" : sClassName;
    }

    public ColorBeans setsClassName(String sClassName) {
        this.sClassName = sClassName;
        return this;
    }

    public String getdStopDate() {
        return StringUtil.getDate(dStopDate, 2);
    }

    public ColorBeans setdStopDate(String dStopDate) {
        this.dStopDate = dStopDate;
        return this;
    }

    public String getsRemark() {
        return TextUtils.isEmpty(sRemark) ? "" : sRemark;
    }

    public ColorBeans setsRemark(String sRemark) {
        this.sRemark = sRemark;
        return this;
    }


    public List<FormColumn> getList() {
        List<FormColumn> list = new ArrayList<>();
        list.add(new FormColumn(row + 1 + "", 0.5f, true, 0, row));
        list.add(new FormColumn(sColorID + "", 1.0f, true, 1, row));
        list.add(new FormColumn(sColorName + "", 1.0f, true, 2, row));
        list.add(new FormColumn(sClassName + "", 1.0f, true, 3, row));
        list.add(new FormColumn(sRemark + "", 3.0f, true, 4, row));
        return list;
    }

    public String paramsFields() {
        return "sColorName" + ",sColorID" + ",sClassID" + ",dStopDate" + ",sRemark";
    }

    public String paramsFieldsValues() {
//        Log.e("remark", sRemark + "qq");
        return sColorName + "," + sColorID + "," + sClassID + "," + dStopDate + "," + sRemark;
    }

    public String paramsFilterFields() {
        return "iRecNo";
    }

    public String paramsFilterComOprts() {
        return "=";
    }

    public String paramsFilterValues() {
        return iRecNo == 0 ? "" : iRecNo + "";
    }

    public String paramsFieldKeys() {
        return "iRecNo";
    }

    public String paramsFieldKeysValues() {
        return iRecNo == 0 ? "" : iRecNo + "";
    }
}
