package com.yyy.fuzhuangpad.sale;

public class BillStyle {
    private int iRecNo;
    private String sStyleNo;
    private String sStyleName;
    private String sClassID;
    private String sClassName;
    private String sReMark;
    private String sGroupName;
    private double fCostPrice;
    private boolean isChecked;

    public String getsGroupName() {
        return sGroupName;
    }

    public void setsGroupName(String sGroupName) {
        this.sGroupName = sGroupName;
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

    public String getsReMark() {
        return sReMark;
    }

    public void setsReMark(String sReMark) {
        this.sReMark = sReMark;
    }

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
