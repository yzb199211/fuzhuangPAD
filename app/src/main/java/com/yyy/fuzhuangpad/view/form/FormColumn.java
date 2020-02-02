package com.yyy.fuzhuangpad.view.form;

public class FormColumn {
    private String text;
    private float weight = 1;
    private boolean isCenter;
    private int col;
    private int row;

    public FormColumn() {
    }

    public FormColumn(String text) {
        this.text = text;
    }

    public FormColumn(String text, boolean isCenter) {
        this.text = text;
        this.isCenter = isCenter;
    }

    public FormColumn(String text, float weight) {
        this.text = text;
        this.weight = weight;
    }

    public FormColumn(String text, float weight, boolean isCenter) {
        this.text = text;
        this.weight = weight;
        this.isCenter = isCenter;
    }

    public FormColumn(String text, float weight, boolean isCenter, int col, int row) {
        this.text = text;
        this.weight = weight;
        this.isCenter = isCenter;
        this.col = col;
        this.row = row;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public boolean isCenter() {
        return isCenter;
    }

    public void setCenter(boolean center) {
        isCenter = center;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
