package com.yyy.fuzhuangpad.sale;

public class BillDetailBean extends BillDetailBase {
    private String sStyleNo;
    private String sColorName;

    public BillDetailBean() {
    }

    public BillDetailBean(int iMainRecNo, int iBscDataStyleMRecNo, String sStyleNo, int iBscDataColorRecNo, String sColorName, String sSizeName, int iSumQty, double fPrice, double fTotal, String sRemark) {
        setiMainRecNo(iMainRecNo);
        setiBscDataStyleMRecNo(iBscDataStyleMRecNo);
        setiBscDataColorRecNo(iBscDataColorRecNo);
        setsSizeName(sSizeName);
        setiSumQty(iSumQty);
        setfPrice(fPrice);
        setfTotal(fTotal);
        setsRemark(sRemark);
        this.sStyleNo = sStyleNo;
        this.sColorName = sColorName;
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

    public static String childtypeParams() {
        return "son";
    }

    public static String tablenameParams() {
        return "SdContractD";
    }

    public static String linkfieldParams() {
        return "iRecNo=iMainRecNo";
    }

    public static String fieldkeyParams() {
        return "iRecNo";
    }
}
