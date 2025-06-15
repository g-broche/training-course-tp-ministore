package com.gbroche.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gbroche.model.Category;
import com.gbroche.service.DatabaseService;

/**
 * Manages requests involving categories in the database
 */
public class CategoryDao {

    private static CategoryDao instance;
    private final DatabaseService databaseService;

    private CategoryDao(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public static synchronized CategoryDao getInstance() {
        if (instance == null) {
            instance = new CategoryDao(DatabaseService.getInstance());
        }
        return instance;
    }

    /**
     * return all existing categories
     * 
     * @return List of categories
     */
    public List<Category> getCategories() {
        List<Category> categoriesFound = new ArrayList<>();
        try (Connection connection = databaseService.getConnection()) {
            String query = "SELECT * FROM categories ORDER BY categoryname";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                categoriesFound.add(
                        new Category(
                                rs.getInt("category"),
                                rs.getString("categoryname")));
            }
            databaseService.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoriesFound;
    }
}
