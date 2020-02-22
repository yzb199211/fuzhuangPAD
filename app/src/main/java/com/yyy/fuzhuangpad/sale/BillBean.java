package com.yyy.fuzhuangpad.sale;

import com.yyy.fuzhuangpad.util.StringUtil;
import com.yyy.fuzhuangpad.view.form.FormColumn;

import java.util.ArrayList;
import java.util.List;

public class BillBean {
    private int iRecNo;
    private String sOrderNo = "";
    private String dDate = "";
    private int iBscdataStockMRecNo;
    private String sStockName = "";
    private int iBscDataCustomerRecNo;
    private String sCustShortName = "";
    private int iOrderType;
    private String sOrderType = "";
    private String dOrderDate = "";
    private String sSaleID = "";
    private String sSaleName = "";
    private String sRemark = "";
    private float fPayMomey;
    private String sPaymethod = "";
    private int iQty;
    private double fTotal;
    private String sStatusName = "";
    private int row;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getiRecNo() {
        return iRecNo;
    }

    public void setiRecNo(int iRecNo) {
        this.iRecNo = iRecNo;
    }

    public String getsOrderNo() {
        return sOrderNo;
    }

    public void setsOrderNo(String sOrderNo) {
        this.sOrderNo = sOrderNo;
    }

    public String getdDate() {
        return StringUtil.getDate(dDate, 2);
    }

    public void setdDate(String dDate) {
        this.dDate = dDate;
    }

    public int getiBscdataStockMRecNo() {
        return iBscdataStockMRecNo;
    }

    public void setiBscdataStockMRecNo(int iBscdataStockMRecNo) {
        this.iBscdataStockMRecNo = iBscdataStockMRecNo;
    }

    public String getsStockName() {
        return sStockName;
    }

    public void setsStockName(String sStockName) {
        this.sStockName = sStockName;
    }

    public int getiBscDataCustomerRecNo() {
        return iBscDataCustomerRecNo;
    }

    public void setiBscDataCustomerRecNo(int iBscDataCustomerRecNo) {
        this.iBscDataCustomerRecNo = iBscDataCustomerRecNo;
    }

    public String getsCustShortName() {
        return sCustShortName;
    }

    public void setsCustShortName(String sCustShortName) {
        this.sCustShortName = sCustShortName;
    }

    public int getiOrderType() {
        return iOrderType;
    }

    public void setiOrderType(int iOrderType) {
        this.iOrderType = iOrderType;
    }

    public String getsOrderType() {
        return sOrderType;
    }

    public void setsOrderType(String sOrderType) {
        this.sOrderType = sOrderType;
    }

    public String getdOrderDate() {
        return StringUtil.getDate(dOrderDate, 2);
    }

    public void setdOrderDate(String dOrderDate) {
        this.dOrderDate = dOrderDate;
    }

    public String getsSaleID() {
        return sSaleID;
    }

    public void setsSaleID(String sSaleID) {
        this.sSaleID = sSaleID;
    }

    public String getsSaleName() {
        return sSaleName;
    }

    public void setsSaleName(String sSaleName) {
        this.sSaleName = sSaleName;
    }

    public String getsRemark() {
        return sRemark;
    }

    public void setsRemark(String sRemark) {
        this.sRemark = sRemark;
    }

    public float getfPayMomey() {
        return fPayMomey;
    }

    public void setfPayMomey(float fPayMomey) {
        this.fPayMomey = fPayMomey;
    }

    public String getsPaymethod() {
        return sPaymethod;
    }

    public void setsPaymethod(String sPaymethod) {
        this.sPaymethod = sPaymethod;
    }

    public int getiQty() {
        return iQty;
    }

    public void setiQty(int iQty) {
        this.iQty = iQty;
    }

    public double getfTotal() {
        return fTotal;
    }

    public void setfTotal(double fTotal) {
        this.fTotal = fTotal;
    }

    public String getsStatusName() {
        return sStatusName;
    }

    public void setsStatusName(String sStatusName) {
        this.sStatusName = sStatusName;
    }

    public static String getFields() {
        return "iRecNo" + "," +
                "sOrderNo" + "," +
                "dDate" + "," +
                "iBscdataStockMRecNo" + "," +
                "sStockName" + "," +
                "iBscDataCustomerRecNo" + "," +
                "sCustShortName" + "," +
                "iOrderType" + "," +
                "sOrderType" + "," +
                "dOrderDate" + "," +
                "sSaleID" + "," +
                "sSaleName" + "," +
                "sRemark" + "," +
                "fPayMomey" + "," +
                "sPaymethod" + "," +
                "iQty" + "," +
                "fTotal" + "," +
                "sStatusName";
    }

    public List<FormColumn> getList() {
        List<FormColumn> list = new ArrayList<>();
        list.add(new FormColumn(row + 1 + "", 0.5f, true, 0, row));
        list.add(new FormColumn(sStatusName + "", 1.0f, true, 1, row));
        list.add(new FormColumn(sOrderNo + "", 1.0f, true, 2, row));
        list.add(new FormColumn(sStockName + "", 1.0f, true, 3, row));
        list.add(new FormColumn(StringUtil.getDate(dDate, 2) + "", 1.0f, true, 4, row));
        list.add(new FormColumn(sCustShortName + "", 1.0f, true, 5, row));
        list.add(new FormColumn(sSaleName + "", 1.0f, true, 6, row));
        list.add(new FormColumn(iQty + "", 1.0f, true, 7, row));
        list.add(new FormColumn(fTotal + "", 1.0f, true, 8, row));
        return list;
    }

    public String paramsFields() {
        return "iRecNo" + "," +
                "sOrderNo" + "," +
                "dDate" + "," +
                "iBscdataStockMRecNo" + "," +
                "iBscDataCustomerRecNo" + "," +
                "iOrderType" + "," +
                "dOrderDate" + "," +
                "sSaleID" + "," +
                "sRemark" + "," +
                "fPayMomey" + "," +
                "sPaymethod" + "," +
                "iQty" + "," +
                "fTotal" + "," + "sUserID";
    }

    public String paramsFieldsValues(String userid) {
        return iRecNo + "," +
                sOrderNo + "," +
                dDate + "," +
                iBscdataStockMRecNo + "," +
                iBscDataCustomerRecNo + "," +
                iOrderType + "," +
                dOrderDate + "," +
                sSaleID + "," +
                sRemark + "," +
                fPayMomey + "," +
                sPaymethod + "," +
                iQty + "," +
                fTotal + "," + userid;
    }

    public String paramsFilterFields() {
        return "iRecNo";
    }

    public String paramsFilterComOprts() {
        return "=";
    }

    public String paramsFilterValues() {
        return iRecNo == 0 ? "" : iRecNo + "";
    }

    public String paramsFieldKeys() {
        return "iRecNo";
    }

    public String paramsFieldKeysValues() {
        return iRecNo == 0 ? "" : iRecNo + "";
    }
}
