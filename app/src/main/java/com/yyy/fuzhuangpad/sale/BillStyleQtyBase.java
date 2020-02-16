package com.yyy.fuzhuangpad.sale;

import androidx.annotation.Nullable;

import java.util.Objects;

public class BillStyleQtyBase {
    private String sColorName;
    private int iBscDataStyleMRecNo;
    private int iBscDataColorRecNo;
    private String sSizeName;
    private int iQty;

    public BillStyleQtyBase() {
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

    public BillStyleQtyBase(String sColorName, int iBscDataColorRecNo, int iBscDataStyleMRecNo, String sSizeName, int iQty) {
        this.sColorName = sColorName;
        this.iBscDataStyleMRecNo = iBscDataStyleMRecNo;
        this.iBscDataColorRecNo = iBscDataColorRecNo;
        this.sSizeName = sSizeName;
        this.iQty = iQty;
    }

    public String getsColorName() {
        return sColorName;
    }

    public void setsColorName(String sColorName) {
        this.sColorName = sColorName;
    }

    public String getsSizeName() {
        return sSizeName;
    }

    public void setsSizeName(String sSizeName) {
        this.sSizeName = sSizeName;
    }

    public int getiQty() {
        return iQty;
    }

    public void setiQty(int iQty) {
        this.iQty = iQty;
    }

//    @Override
//    public boolean equals(@Nullable Object obj) {
//        if (this == obj) return true;
//        if (obj == null || getClass() != obj.getClass()) return false;
//        BillStyleQtyBase billStyleQtyBase = (BillStyleQtyBase) obj;
//        return iBscDataColorRecNo == billStyleQtyBase.iBscDataColorRecNo &&
//                iBscDataStyleMRecNo == billStyleQtyBase.iBscDataStyleMRecNo &&
//                sSizeName.equals(billStyleQtyBase.sSizeName);
//    }
}
