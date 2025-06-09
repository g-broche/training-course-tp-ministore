package com.gbroche.model;

import java.util.Date;

public class Order {

    private Integer id;
    private Integer customerId;
    private Double netAmount;
    private Double tax;
    private Double totalAmount;
    private Date date;

    public Order(Integer id, Integer customerId, Double netAmount, Double tax, Double totalAmount, Date date) {
        this.id = id;
        this.customerId = customerId;
        this.netAmount = netAmount;
        this.tax = tax;
        this.totalAmount = totalAmount;
        this.date = date;
    }

    public Order(Integer customerId, Double netAmount, Double tax, Double totalAmount, Date date) {
        this.customerId = customerId;
        this.netAmount = netAmount;
        this.tax = tax;
        this.totalAmount = totalAmount;
        this.date = date;
    }

}
