package com.yyy.fuzhuangpad.sale;

import com.yyy.fuzhuangpad.style.StyleColor;

import java.util.ArrayList;
import java.util.List;

public class BillColor extends StyleColor {
    List<BillStyleQty> styleQty = new ArrayList<>();

    public List<BillStyleQty> getStyleQty() {
        return styleQty;
    }

    public void setStyleQty(List<BillStyleQty> styleQty) {
        this.styleQty.clear();
        this.styleQty.addAll(styleQty);
    }
}
