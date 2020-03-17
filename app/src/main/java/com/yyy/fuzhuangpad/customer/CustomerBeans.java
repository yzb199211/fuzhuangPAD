package com.yyy.fuzhuangpad.customer;

import com.yyy.fuzhuangpad.dialog.ISelectText;
import com.yyy.fuzhuangpad.util.StringUtil;
import com.yyy.fuzhuangpad.view.form.FormColumn;

import java.util.ArrayList;
import java.util.List;

public class CustomerBeans implements ISelectText {
    private int iRecNo;
    private String sCustID = "";
    private String sCustName = "";
    private String sCustShortName = "";
    private String sClassID = "";
    private String sClassName = "";
    private String sSaleID = "";
    private String sSaleName = "";
    private String sPerson = "";
    private String sTel = "";
    private String sAddress = "";
    private String dStopDate = "";
    private String sRemark = "";
    private int iCustType;
    private int row;

    public int getiRecNo() {
        return iRecNo;
    }

    public void setiRecNo(int iRecNo) {
        this.iRecNo = iRecNo;
    }

    public String getsCustID() {
        return sCustID;
    }

    public void setsCustID(String sCustID) {
        this.sCustID = sCustID;
    }

    public String getsCustName() {
        return sCustName;
    }

    public void setsCustName(String sCustName) {
        this.sCustName = sCustName;
    }

    public String getsCustShortName() {
        return sCustShortName;
    }

    public void setsCustShortName(String sCustShortName) {
        this.sCustShortName = sCustShortName;
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

    public String getsSaleID() {
        return sSaleID;
    }

    public void setsSaleID(String sSaleID) {
        this.sSaleID = sSaleID;
    }

    public String getsSaleName() {
        return sSaleName;
    }

    public void setsSaleName(String sSaleName) {
        this.sSaleName = sSaleName;
    }

    public String getsPerson() {
        return sPerson;
    }

    public void setsPerson(String sPerson) {
        this.sPerson = sPerson;
    }

    public String getsTel() {
        return sTel;
    }

    public void setsTel(String sTel) {
        this.sTel = sTel;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getdStopDate() {
        return StringUtil.getDate(dStopDate, 2);
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

    public int getiCustType() {
        return iCustType;
    }

    public void setiCustType(int iCustType) {
        this.iCustType = iCustType;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public List<FormColumn> getList() {
        List<FormColumn> list = new ArrayList<>();
        list.add(new FormColumn(row + 1 + "", 0.5f, true, 0, row));
        list.add(new FormColumn(sCustID, 1.0f, true, 1, row));
        list.add(new FormColumn(sCustShortName, 1.0f, true, 2, row));
        list.add(new FormColumn(sSaleName, 1.0f, true, 3, row));
        list.add(new FormColumn(sClassName, 1.0f, true, 4, row));
        list.add(new FormColumn(sPerson, 1.0f, true, 5, row));
        list.add(new FormColumn(sTel, 1.0f, true, 6, row));
        return list;
    }

    public List<FormColumn> getSelectList() {
        List<FormColumn> list = new ArrayList<>();
        list.add(new FormColumn(row + 1 + "", 0.5f, true, 0, row));
        list.add(new FormColumn(sCustID, 1.0f, true, 1, row));
        list.add(new FormColumn(sCustShortName, 1.0f, true, 2, row));
        list.add(new FormColumn(sSaleName, 1.0f, true, 3, row));
        list.add(new FormColumn(sClassName, 1.0f, true, 4, row));
        list.add(new FormColumn(sPerson, 1.0f, true, 5, row));
        return list;
    }

    public String paramsFields() {
        return "sCustName" + ",sCustID" + ",sCustShortName" + ",sClassID" + ",sSaleID" + ",sPerson" + ",sTel" + ",sAddress" + ",dStopDate" + ",sRemark" + ",iCustType";
    }

    public String paramsFieldsValues() {
        return sCustName + "," + sCustID + "," + sCustShortName + ","
                + sClassID + "," + sSaleID + ","
                + sPerson + "," + sTel + "," + sAddress + "," + dStopDate + "," + sRemark + ",0";
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

    public void copy(CustomerBeans customer) {
        iRecNo = customer.getiRecNo();
        sCustID = customer.getsCustID();
        sCustName = customer.getsCustName();
        sCustShortName = customer.getsCustShortName();
        sClassID = customer.getsClassID();
        sClassName = customer.getsClassName();
        sSaleID = customer.getsSaleID();
        sSaleName = customer.getsSaleName();
        sPerson = customer.getsPerson();
        sTel = customer.getsTel();
        sAddress = customer.getsAddress();
        dStopDate = customer.getdStopDate();
        sRemark = customer.getsRemark();
    }

    @Override
    public String getText() {
        return sCustName + "|" + sCustShortName;
    }
}
