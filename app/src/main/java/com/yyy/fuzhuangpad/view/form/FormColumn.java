package com.yyy.fuzhuangpad.view.form;

public class FormColumn {
    private String text;
    private float weight = 1;
    private boolean isCenter;

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
}
