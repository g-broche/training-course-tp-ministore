package com.gbroche.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gbroche.model.Product;
import com.gbroche.service.DatabaseService;

public class ProductDao {

    private static ProductDao instance;
    private final DatabaseService databaseService;

    private ProductDao(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ProductDao(DatabaseService.getInstance());
        }
        return instance;
    }

    public List<Product> getAllProducts() {
        List<Product> countriesFound = new ArrayList<>();
        try (Connection connection = databaseService.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT p.*, c.categoryname, i.quan_in_stock FROM products p JOIN categories c ON p.category=c.category JOIN inventory i ON p.prod_id = i.prod_id ORDER BY title");
            while (rs.next()) {
                countriesFound.add(
                        new Product(
                                rs.getInt("prod_id"),
                                rs.getString("categoryname"),
                                rs.getString("title"),
                                rs.getString("actor"),
                                rs.getInt("quan_in_stock"),
                                rs.getDouble("price"),
                                rs.getInt("special")));
            }
            databaseService.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countriesFound;
    }

    public boolean decreaseStock(int productId, int amount, Connection connection) {
        boolean isSuccess = false;
        String sql = "UPDATE inventory SET quan_in_stock = quan_in_stock - ? WHERE prod_id = ? AND quan_in_stock >= ? RETURNING quan_in_stock";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, amount);
            stmt.setInt(2, productId);
            stmt.setInt(3, amount);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int newStock = rs.getInt("quan_in_stock");
                    isSuccess = true;
                } else {
                    throw new Exception();
                }
            }
            return isSuccess;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
