package com.yyy.fuzhuangpad.sale;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class BillDetailBean extends BillDetailBase implements Comparable<BillDetailBean> {
    private String sStyleNo;
    private String sColorName;
    private int iSerial;

    public BillDetailBean() {
    }

    public BillDetailBean(int iMainRecNo, int iBscDataStyleMRecNo, String sStyleNo, int iBscDataColorRecNo, String sColorName, String sSizeName, int iSumQty, double fPrice, double fTotal, String sRemark, int iSerial) {
        setiMainRecNo(iMainRecNo);
        setiBscDataStyleMRecNo(iBscDataStyleMRecNo);
        setiBscDataColorRecNo(iBscDataColorRecNo);
        setsSizeName(sSizeName);
        setiSumQty(iSumQty);
        setfPrice(fPrice);
        setfTotal(fTotal);
        setsRemark(sRemark);
        this.iSerial = iSerial;
        this.sStyleNo = sStyleNo;
        this.sColorName = sColorName;
    }

    public int getiSerial() {
        return iSerial;
    }

    public void setiSerial(int iSerial) {
        this.iSerial = iSerial;
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

    @Override
    public int compareTo(BillDetailBean o) {
        if (o.getiBscDataStyleMRecNo() - getiBscDataStyleMRecNo() != 0) {
            return getiBscDataStyleMRecNo() - o.getiBscDataStyleMRecNo();
        } else if (getiBscDataColorRecNo() - o.getiBscDataColorRecNo() != 0) {
            return getiBscDataColorRecNo() - o.getiBscDataColorRecNo();
        } else {
            return iSerial - o.iSerial;
        }
    }
}
