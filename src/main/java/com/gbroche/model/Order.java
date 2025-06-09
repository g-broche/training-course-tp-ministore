package com.gbroche.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {

    private Integer id;
    private Integer customerId;
    private Double netAmount;
    private Double tax;
    private Double totalAmount;
    private Date date;
    private List<OrderLine> lines;

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

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public Double getNetAmount() {
        return netAmount;
    }

    public Double getTax() {
        return tax;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public Date getDate() {
        return date;
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    public void getLines(List<OrderLine> orderLines) {
        lines = orderLines;
    }

    public void addLine(OrderLine orderLine) {
        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.add(orderLine);
    }
}
