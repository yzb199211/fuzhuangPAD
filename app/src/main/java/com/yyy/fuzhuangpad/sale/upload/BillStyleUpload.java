package com.yyy.fuzhuangpad.sale.upload;

import androidx.annotation.Nullable;

import com.yyy.fuzhuangpad.util.StringUtil;

public class BillStyleUpload {
    private int iRecNo = 0;
    private int iMainRecNo = 0;
    private int iBscDataStyleMRecNo = 0;
    private int iBscDataColorRecNo = 0;
    private int iSumQty = 0;
    private double fPrice = 0;
    private double fTotal = 0;

    public BillStyleUpload() {
    }

    public BillStyleUpload(int iMainRecNo, int iBscDataStyleMRecNo, int iBscDataColorRecNo, int iSumQty, double fPrice, double fTotal) {
        this.iMainRecNo = iMainRecNo;
        this.iBscDataStyleMRecNo = iBscDataStyleMRecNo;
        this.iBscDataColorRecNo = iBscDataColorRecNo;
        this.iSumQty = iSumQty;
        this.fTotal = fTotal;
        this.fPrice = fPrice;
    }

    public double getfPrice() {
        return fPrice;
    }

    public void setfPrice(double fPrice) {
        this.fPrice = fPrice;
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

    public int getiSumQty() {
        return iSumQty;
    }

    public void setiSumQty(int iSumQty) {
        this.iSumQty = iSumQty;
    }

    public double getfTotal() {
        return fTotal;
    }

    public void setfTotal(double fTotal) {
        this.fTotal = fTotal;
    }

    public void sum(int iSumQty, double fTotal) {
        this.iSumQty = this.iSumQty + iSumQty;
        this.fTotal = StringUtil.add(this.fTotal, fTotal);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BillStyleUpload billStyleUpload = (BillStyleUpload) obj;
        return iBscDataColorRecNo == billStyleUpload.iBscDataColorRecNo &&
                iBscDataStyleMRecNo == billStyleUpload.iBscDataStyleMRecNo;
    }
}
