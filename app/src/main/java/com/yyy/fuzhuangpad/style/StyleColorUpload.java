package com.yyy.fuzhuangpad.style;

public class StyleColorUpload {
    private int iRecNo;
//    private String sColorName;
    private int iBscDataColorRecNo = 0;
    private int iMainRecNo;

    public int getiRecNo() {
        return iRecNo;
    }

    public void setiRecNo(int iRecNo) {
        this.iRecNo = iRecNo;
    }

//    public String getsColorName() {
//        return sColorName;
//    }
//
//    public void setsColorName(String sColorName) {
//        this.sColorName = sColorName;
//    }

    public int getiBscDataColorRecNo() {
        return iBscDataColorRecNo;
    }

    public void setiBscDataColorRecNo(int iBscDataColorRecNo) {
        this.iBscDataColorRecNo = iBscDataColorRecNo;
    }

    public int getiMainRecNo() {
        return iMainRecNo;
    }

    public void setiMainRecNo(int iMainRecNo) {
        this.iMainRecNo = iMainRecNo;
    }

    public static String childtypeParams() {
        return "son";
    }

    public static String tablenameParams() {
        return "BscDataStyleDColor";
    }

    public static String linkfieldParams() {
        return "iRecNo=iMainRecNo";
    }

    public static String fieldkeyParams() {
        return "iRecNo";
    }
}
