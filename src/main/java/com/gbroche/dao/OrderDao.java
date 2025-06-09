package com.gbroche.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import com.gbroche.model.OrderLine;
import com.gbroche.service.DatabaseService;

public class OrderDao {

    private static OrderDao instance;
    private final DatabaseService databaseService;

    private OrderDao(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public static synchronized OrderDao getInstance() {
        if (instance == null) {
            instance = new OrderDao(DatabaseService.getInstance());
        }
        return instance;
    }

    public Integer createNewOrder(int customerId, LocalDate date, Double priceWithoutTaxes, Double tax,
            Double priceWithTaxes, Connection connection) {

        String insertOrderSQL = """
                INSERT INTO orders (customerid, orderdate, netamount, tax, totalamount)
                VALUES (?, ?, ?, ?, ?)
                RETURNING orderid
                """;
        try {
            PreparedStatement stmt = connection.prepareStatement(insertOrderSQL);
            stmt.setInt(1, customerId);
            stmt.setDate(2, java.sql.Date.valueOf(date));
            stmt.setDouble(3, priceWithoutTaxes);
            stmt.setDouble(4, tax);
            stmt.setDouble(5, priceWithTaxes);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int orderId = rs.getInt("orderid");
                return orderId;
            } else {
                throw new Exception("Failed to get id of new order");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public boolean addOrderLineToOrder(int lineId, Integer orderId, OrderLine orderLine, LocalDate orderDate,
            Connection connection) {
        String insertOrderSQL = """
                INSERT INTO orderlines (orderlineid, orderid, prod_id, quantity, orderdate)
                VALUES (?, ?, ?, ?, ?)
                """;
        try {
            Integer productId = orderLine.getProduct().getId();
            Integer quantity = orderLine.getProduct().getQuantity();
            if (orderId == null || orderId <= 0 || productId <= 0 || quantity <= 0 || orderDate == null) {
                throw new IOException("Invalid values for inserting a new order line");
            }
            PreparedStatement stmt = connection.prepareStatement(insertOrderSQL);
            stmt.setInt(1, lineId);
            stmt.setInt(2, orderId);
            stmt.setInt(3, productId);
            stmt.setInt(4, quantity);
            stmt.setDate(5, java.sql.Date.valueOf(orderDate));
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}
