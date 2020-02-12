package com.yyy.fuzhuangpad.sale;

public class BillStyle {
    private int iRecNo;
    private String sStyleNo;
    private String sClassName;
    private double fCostPrice;
    private boolean isChecked;

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

    public String getsClassName() {
        return sClassName;
    }

    public void setsClassName(String sClassName) {
        this.sClassName = sClassName;
    }

    public double getfCostPrice() {
        return fCostPrice;
    }

    public void setfCostPrice(double fCostPrice) {
        this.fCostPrice = fCostPrice;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
