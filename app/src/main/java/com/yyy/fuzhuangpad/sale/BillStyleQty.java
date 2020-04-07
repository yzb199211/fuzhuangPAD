package com.yyy.fuzhuangpad.sale;

public class BillStyleQty extends BillStyleQtyBase {

    private int num = 0;
    private double price;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
