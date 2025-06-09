package com.gbroche.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            ResultSet rs = stmt.executeQuery("SELECT * FROM customers ORDER BY customerid DESC LIMIT 5");
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
                                rs.getInt("region")));
            }
            databaseService.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customersFound;
    }

    public List<Customer> getCustomersWithOrderHistory() {
        List<Customer> customersFound = new ArrayList<>();
        try (Connection connection = databaseService.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(
                    """
                            SELECT DISTINCT c.*
                            FROM customers c
                            JOIN orders o ON c.customerid = o.customerid
                            ORDER BY customerid DESC
                            """);
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
                                rs.getInt("region")));
            }
            databaseService.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customersFound;
    }

    public boolean addCustomer(Map<String, String> inputs) {
        try (Connection connection = databaseService.getConnection()) {
            String query = "{ ? = call new_customer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
            CallableStatement stmt = connection.prepareCall(query);
            stmt.registerOutParameter(1, java.sql.Types.INTEGER);
            stmt.setString(2, inputs.get("firstname"));
            stmt.setString(3, inputs.get("lastname"));
            stmt.setString(4, inputs.get("address1"));
            stmt.setString(5, inputs.get("address2"));
            stmt.setString(6, inputs.get("city"));
            stmt.setString(7, inputs.get("state"));
            stmt.setInt(8, Integer.parseInt(inputs.get("zip")));
            stmt.setString(9, inputs.get("country"));
            stmt.setInt(10, Integer.parseInt(inputs.get("region")));
            stmt.setString(11, inputs.get("email"));
            stmt.setString(12, inputs.get("phone"));
            stmt.setInt(13, Integer.parseInt(inputs.get("creditcardtype")));
            stmt.setString(14, inputs.get("creditcard"));
            stmt.setString(15, inputs.get("creditcardexpiration"));
            stmt.setString(16, inputs.get("username"));
            stmt.setString(17, inputs.get("password"));
            stmt.setInt(18, Integer.parseInt(inputs.get("age")));
            stmt.setInt(19, Integer.parseInt(inputs.get("income")));
            stmt.setString(20, inputs.get("gender"));
            stmt.execute();
            int newId = stmt.getInt(1);
            boolean isSuccess = newId > 0;
            if (!isSuccess) {
                throw new Exception(
                        "Failed to create user, an existing user may already have data conflicting with provided data for new customer");
            }
            System.out.println("Created new customer with ID: " + newId);
            databaseService.closeConnection();
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to create new customer, SQLerror: '" + e.getMessage() + "'");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("Failed to create new customer, error: '" + e.getMessage() + "'");
            e.printStackTrace();
            return false;
        }
    }
}
