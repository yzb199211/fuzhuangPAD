package com.yyy.fuzhuangpad.style;

import com.yyy.fuzhuangpad.view.form.FormColumn;

import java.util.ArrayList;
import java.util.List;

public class StyleBeans {
    private int iRecNo;
    private String sStyleNo;
    private String sStyleName;
    private String sClassID;
    private String sClassName;
    private String sSizeGroupID;
    private String sGroupName;
    private int iBscDataCustomerRecNo;
    private String sCustShortName;
    private String iYear;
    private double fCostPrice;
    private double fBulkTotal1;
    private double fSalePrice;
    private String sCustStyleNo;
    private String sWaterElents;
    private String sReMark;
    private double dStopDate;
    private int row;


    public int getiRecNo() {
        return iRecNo;
    }

    public void setiRecNo(int iRecNo) {
        this.iRecNo = iRecNo;
    }

    public String getsStyleNo() {
        return sStyleNo;
    }

    public void setsStyleNo(String sStyleNo) {
        this.sStyleNo = sStyleNo;
    }

    public String getsStyleName() {
        return sStyleName;
    }

    public void setsStyleName(String sStyleName) {
        this.sStyleName = sStyleName;
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

    public String getsSizeGroupID() {
        return sSizeGroupID;
    }

    public void setsSizeGroupID(String sSizeGroupID) {
        this.sSizeGroupID = sSizeGroupID;
    }

    public String getsGroupName() {
        return sGroupName;
    }

    public void setsGroupName(String sGroupName) {
        this.sGroupName = sGroupName;
    }

    public int getiBscDataCustomerRecNo() {
        return iBscDataCustomerRecNo;
    }

    public void setiBscDataCustomerRecNo(int iBscDataCustomerRecNo) {
        this.iBscDataCustomerRecNo = iBscDataCustomerRecNo;
    }

    public String getsCustShortName() {
        return sCustShortName;
    }

    public void setsCustShortName(String sCustShortName) {
        this.sCustShortName = sCustShortName;
    }

    public String getiYear() {
        return iYear;
    }

    public void setiYear(String iYear) {
        this.iYear = iYear;
    }

    public double getfCostPrice() {
        return fCostPrice;
    }

    public void setfCostPrice(double fCostPrice) {
        this.fCostPrice = fCostPrice;
    }

    public double getfBulkTotal1() {
        return fBulkTotal1;
    }

    public void setfBulkTotal1(double fBulkTotal1) {
        this.fBulkTotal1 = fBulkTotal1;
    }

    public double getfSalePrice() {
        return fSalePrice;
    }

    public void setfSalePrice(double fSalePrice) {
        this.fSalePrice = fSalePrice;
    }

    public String getsCustStyleNo() {
        return sCustStyleNo;
    }

    public void setsCustStyleNo(String sCustStyleNo) {
        this.sCustStyleNo = sCustStyleNo;
    }

    public String getsWaterElents() {
        return sWaterElents;
    }

    public void setsWaterElents(String sWaterElents) {
        this.sWaterElents = sWaterElents;
    }

    public String getsReMark() {
        return sReMark;
    }

    public void setsReMark(String sReMark) {
        this.sReMark = sReMark;
    }

    public double getdStopDate() {
        return dStopDate;
    }

    public void setdStopDate(double dStopDate) {
        this.dStopDate = dStopDate;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public List<FormColumn> getList() {
        List<FormColumn> list = new ArrayList<>();
        list.add(new FormColumn(row+1 + "", 0.5f, true, 0, row));
        list.add(new FormColumn(sStyleNo,1.0f,true,1,row));
        list.add(new FormColumn(sStyleName,1.0f,true,2,row));
        list.add(new FormColumn(sClassName,1.0f,true,3,row));
        list.add(new FormColumn(sGroupName,1.0f,true,4,row));
        list.add(new FormColumn(sCustShortName,1.0f,true,5,row));
        list.add(new FormColumn(iYear,1.0f,true,6,row));
        list.add(new FormColumn(fCostPrice+"",1.0f,true,7,row));
        list.add(new FormColumn(fBulkTotal1+"",1.0f,true,8,row));
        return list;
    }
}
