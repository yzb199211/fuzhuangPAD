package com.yyy.fuzhuangpad.sale;

public class BillDetailBean {
    private int iRecNo;
    private int iMainRecNo;
    private int iBscDataStyleMRecNo;
    private int iBscDataColorRecNo;
    private String sSizeName;
    private int iSumQty;
    private double fPrice;
    private double fTotal;
    private String sRemark;
    private String sStyleNo;
    private String sColorName;

    public BillDetailBean() {
    }

    public BillDetailBean(int iMainRecNo, int iBscDataStyleMRecNo, String sStyleNo, int iBscDataColorRecNo, String sColorName, String sSizeName, int iSumQty, double fPrice, double fTotal, String sRemark) {
        this.iMainRecNo = iMainRecNo;
        this.iBscDataStyleMRecNo = iBscDataStyleMRecNo;
        this.iBscDataColorRecNo = iBscDataColorRecNo;
        this.sSizeName = sSizeName;
        this.iSumQty = iSumQty;
        this.fPrice = fPrice;
        this.fTotal = fTotal;
        this.sRemark = sRemark;
        this.sStyleNo = sStyleNo;
        this.sColorName = sColorName;
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

    public void setfPrice(float fPrice) {
        this.fPrice = fPrice;
    }

    public double getfTotal() {
        return fTotal;
    }

    public void setfTotal(float fTotal) {
        this.fTotal = fTotal;
    }

    public String getsRemark() {
        return sRemark;
    }

    public void setsRemark(String sRemark) {
        this.sRemark = sRemark;
    }

    public String getsStyleNo() {
        return sStyleNo;
    }

    public void setsStyleNo(String sStyleNo) {
        this.sStyleNo = sStyleNo;
    }

    public String getsColorName() {
        return sColorName;
    }

    public void setsColorName(String sColorName) {
        this.sColorName = sColorName;
    }
}
