package com.gbroche.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gbroche.model.Order;
import com.gbroche.model.OrderLine;
import com.gbroche.model.Product;
import com.gbroche.service.DatabaseService;

/**
 * Manages requests involving orders in the database
 */
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

    /**
     * Inserts a new order in the database
     * 
     * @param customerId
     * @param date
     * @param priceWithoutTaxes
     * @param tax
     * @param priceWithTaxes
     * @param connection
     * @return id of created order
     */
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

    /**
     * Inserts order line related to an order
     * 
     * @param lineId
     * @param orderId
     * @param orderLine
     * @param orderDate
     * @param connection
     * @return
     */
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

    /**
     * Retrieves all orders related to a given customer
     * 
     * @param customerId
     * @param connection connection to db
     * @return list of orders
     */
    public List<Order> getOrderHistoryByViewerId(int customerId, Connection connection) {
        List<Order> retrievedOrders = new ArrayList<>();
        String query = """
                SELECT
                    o.orderid,
                    o.orderdate,
                    o.netamount,
                    o.tax,
                    o.totalamount,
                    ol.orderid AS line_order_id,
                    ol.orderlineid,
                    ol.quantity,
                    ol.orderdate AS line_order_date,
                    p.prod_id AS product_id,
                    p.title AS product_title,
                    p.category AS product_category,
                    p.actor AS product_actor
                FROM orders o
                JOIN orderlines ol ON o.orderid = ol.orderid
                JOIN products p ON ol.prod_id = p.prod_id
                WHERE o.customerid = ?
                ORDER BY o.orderid DESC, ol.orderlineid ASC
                """;

        try (connection) {
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            // parse lines returned by the database to group order lines by orders
            retrievedOrders = parseRetrievedOrderHistory(rs);
            connection.close();
            return retrievedOrders;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Iterates through a result sets of order data to parse it and create orders
     * instances with their related order lines
     * 
     * @param rs result set
     * @return list of orders
     * @throws SQLException
     */
    private List<Order> parseRetrievedOrderHistory(ResultSet rs) throws SQLException {
        List<Order> retrievedOrders = new ArrayList<>();
        // declare a lastOrderId that will help determines if the result set line
        // concerns a new order to not
        Integer lastOrderId = null;
        while (rs.next()) {
            int orderId = rs.getInt("orderid");
            Order orderToEdit;
            // if the order id of the current line is different from the previous one,
            // create a new Order
            if (lastOrderId == null || orderId != lastOrderId) {
                orderToEdit = new Order(
                        orderId,
                        rs.getDouble("netamount"),
                        rs.getDouble("tax"),
                        rs.getDouble("totalamount"),
                        rs.getDate("orderdate"));
                retrievedOrders.add(orderToEdit);
                // if still same order, retrieve the last created one to add a new order line
            } else {
                orderToEdit = retrievedOrders.get(retrievedOrders.size() - 1);
            }
            // extract the product data to create a new product
            Product orderedProduct = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_category"),
                    rs.getString("product_title"),
                    rs.getString("product_actor"));
            // extract the order line data to create a new order line
            OrderLine newLine = new OrderLine(
                    rs.getInt("orderlineid"),
                    rs.getInt("line_order_id"),
                    orderedProduct,
                    rs.getInt("quantity"),
                    rs.getDate("line_order_date"));
            // add order line to order
            orderToEdit.addLine(newLine);
            // set lastOrderId to current orderId for next loop comparison
            lastOrderId = orderId;
        }
        return retrievedOrders;
    }
}
