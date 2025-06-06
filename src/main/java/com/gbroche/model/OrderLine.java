package com.gbroche.model;

import java.util.Date;

public class OrderLine {

    private Integer id;
    private Integer orderid;
    private final Product product;
    private final int quantity;
    private Date date;

    public OrderLine(int id, int orderid, Product product, int quantity, Date date) {
        this.id = id;
        this.orderid = orderid;
        this.product = product;
        this.quantity = quantity;
        this.date = date;
    }

    public OrderLine(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getDate() {
        return date;
    }
}
