package com.yyy.fuzhuangpad.sale;

import androidx.annotation.Nullable;

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return getiBscDataColorRecNo() == ((BillColor) obj).getiBscDataColorRecNo();
    }
}
