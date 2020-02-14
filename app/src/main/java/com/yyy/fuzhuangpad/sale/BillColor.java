package com.yyy.fuzhuangpad.sale;

import com.yyy.fuzhuangpad.style.StyleColor;

public class BillColor extends StyleColor {
    private int iBscDataColorRecNo;

    public int getiBscDataColorRecNo() {
        return iBscDataColorRecNo;
    }

    public void setiBscDataColorRecNo(int iBscDataColorRecNo) {
        this.iBscDataColorRecNo = iBscDataColorRecNo;
    }

    @Override
    public String getsColorID() {
        return iBscDataColorRecNo + "";
    }
}
