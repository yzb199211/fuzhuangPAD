package com.yyy.fuzhuangpad.sale;

import androidx.annotation.Nullable;

public class BillDetailBase {
    private int iRecNo;
    private int iMainRecNo;
    private int iBscDataStyleMRecNo;
    private int iBscDataColorRecNo;
    private String sSizeName;
    private int iSumQty;
    private double fPrice;
    private double fTotal;
    private String sRemark;

    public BillDetailBase() {
    }

    public BillDetailBase(int iMainRecNo, int iBscDataStyleMRecNo, String sStyleNo, int iBscDataColorRecNo, String sColorName, String sSizeName, int iSumQty, double fPrice, double fTotal, String sRemark) {
        this.iMainRecNo = iMainRecNo;
        this.iBscDataStyleMRecNo = iBscDataStyleMRecNo;
        this.iBscDataColorRecNo = iBscDataColorRecNo;
        this.sSizeName = sSizeName;
        this.iSumQty = iSumQty;
        this.fPrice = fPrice;
        this.fTotal = fTotal;
        this.sRemark = sRemark;

    }


    public int getiRecNo() {
        return iRecNo;
    }

    public void setiRecNo(int iRecNo) {
        this.iRecNo = iRecNo;
    }

    public int getiMainRecNo() {
        return iMainRecNo;
    }

    public void setiMainRecNo(int iMainRecNo) {
        this.iMainRecNo = iMainRecNo;
    }

    public int getiBscDataStyleMRecNo() {
        return iBscDataStyleMRecNo;
    }

    public void setiBscDataStyleMRecNo(int iBscDataStyleMRecNo) {
        this.iBscDataStyleMRecNo = iBscDataStyleMRecNo;
    }

    public int getiBscDataColorRecNo() {
        return iBscDataColorRecNo;
    }

    public void setiBscDataColorRecNo(int iBscDataColorRecNo) {
        this.iBscDataColorRecNo = iBscDataColorRecNo;
    }

    public String getsSizeName() {
        return sSizeName;
    }

    public void setsSizeName(String sSizeName) {
        this.sSizeName = sSizeName;
    }

    public int getiSumQty() {
        return iSumQty;
    }

    public void setiSumQty(int iSumQty) {
        this.iSumQty = iSumQty;
    }

    public double getfPrice() {
        return fPrice;
    }

    public void setfPrice(double fPrice) {
        this.fPrice = fPrice;
    }

    public double getfTotal() {
        return fTotal;
    }

    public void setfTotal(double fTotal) {
        this.fTotal = fTotal;
    }

    public String getsRemark() {
        return sRemark;
    }

    public void setsRemark(String sRemark) {
        this.sRemark = sRemark;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BillDetailBase billDetailBase = (BillDetailBase) obj;
        return iBscDataColorRecNo == billDetailBase.iBscDataColorRecNo &&
                iBscDataStyleMRecNo == billDetailBase.iBscDataStyleMRecNo
                && sSizeName.equals(billDetailBase.sSizeName);
    }


}
