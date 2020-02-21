package com.yyy.fuzhuangpad.sale.upload;

public class BillSizeUpload {
    private String sSizeName;
    private int iQty;

    public BillSizeUpload() {
    }

    public BillSizeUpload(String sSizeName, int iQty) {
        this.sSizeName = sSizeName;
        this.iQty = iQty;
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
}
