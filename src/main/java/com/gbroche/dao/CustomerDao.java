package com.gbroche.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gbroche.model.Customer;
import com.gbroche.service.DatabaseService;

public class CustomerDao {

    private static CustomerDao instance;
    private final DatabaseService databaseService;

    private CustomerDao(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public static synchronized CustomerDao getInstance() {
        if (instance == null) {
            instance = new CustomerDao(DatabaseService.getInstance());
        }
        return instance;
    }

    public List<Customer> getCustomers() {
        List<Customer> customersFound = new ArrayList<>();
        try (Connection connection = databaseService.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customers LIMIT 5");
            while (rs.next()) {
                customersFound.add(
                        new Customer(
                                rs.getInt("customerid"),
                                rs.getString("firstname"),
                                rs.getString("lastname"),
                                rs.getString("address1"),
                                rs.getString("address2"),
                                rs.getString("city"),
                                rs.getString("state"),
                                rs.getInt("zip"),
                                rs.getString("country"),
                                rs.getInt("region")
                        )
                );
            }
            databaseService.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customersFound;
    }
}
