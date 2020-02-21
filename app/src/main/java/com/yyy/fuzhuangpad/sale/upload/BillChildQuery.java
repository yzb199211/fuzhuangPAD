package com.yyy.fuzhuangpad.sale.upload;

import com.yyy.fuzhuangpad.util.net.MainQueryChild;

import java.util.List;

public class BillChildQuery extends MainQueryChild {
    private List<List<BillSizeUpload>> grandsondata;
    private String grandson = "SdContractDD";

    public List<List<BillSizeUpload>> getGrandsondata() {
        return grandsondata;
    }

    public void setGrandsondata(List<List<BillSizeUpload>> grandsondata) {
        this.grandsondata = grandsondata;
    }

    public String getGrandson() {
        return grandson;
    }

    public void setGrandson(String grandson) {
        this.grandson = grandson;
    }
}
