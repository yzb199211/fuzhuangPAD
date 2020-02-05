package com.yyy.fuzhuangpad.color;

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

    public void setiRecNo(int iRecNo) {
        this.iRecNo = iRecNo;
    }

    public String getsColorID() {
        return sColorID;
    }

    public void setsColorID(String sColorID) {
        this.sColorID = sColorID;
    }

    public String getsColorName() {
        return sColorName;
    }

    public void setsColorName(String sColorName) {
        this.sColorName = sColorName;
    }

    public String getsClassID() {
        return sClassID;
    }

    public void setsClassID(String sClassID) {
        this.sClassID = sClassID;
    }

    public String getsClassName() {
        return sClassName;
    }

    public void setsClassName(String sClassName) {
        this.sClassName = sClassName;
    }

    public String getdStopDate() {
        return dStopDate;
    }

    public void setdStopDate(String dStopDate) {
        this.dStopDate = dStopDate;
    }

    public String getsRemark() {
        return sRemark;
    }

    public void setsRemark(String sRemark) {
        this.sRemark = sRemark;
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
        return sColorName + "," + sColorID + "," + sClassID  + "," + dStopDate + "," + sRemark;
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
