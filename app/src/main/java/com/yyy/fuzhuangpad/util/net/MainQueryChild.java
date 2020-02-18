package com.yyy.fuzhuangpad.util.net;

import com.yyy.fuzhuangpad.style.StyleColorUpload;

import java.util.List;

public class MainQueryChild<T> {
    private String childtype;
    private String tablename;
    private String linkfield;
    private String fieldkey;
    private List<T> data;
    private int[] deleteKey;

    public String getChildtype() {
        return childtype;
    }

    public void setChildtype(String childtype) {
        this.childtype = childtype;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getLinkfield() {
        return linkfield;
    }

    public void setLinkfield(String linkfield) {
        this.linkfield = linkfield;
    }

    public String getFieldkey() {
        return fieldkey;
    }

    public void setFieldkey(String fieldkey) {
        this.fieldkey = fieldkey;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int[] getDeleteKey() {
        return deleteKey;
    }

    public void setDeleteKey(int[] deleteKey) {
        this.deleteKey = deleteKey;
    }
}
