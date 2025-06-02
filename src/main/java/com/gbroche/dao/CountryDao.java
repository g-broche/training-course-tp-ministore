package com.gbroche.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.gbroche.model.Country;
import com.gbroche.service.DatabaseService;

public class CountryDao {

    private static CountryDao instance;
    private final DatabaseService databaseService;

    private CountryDao(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public static synchronized CountryDao getInstance() {
        if (instance == null) {
            instance = new CountryDao(DatabaseService.getInstance());
        }
        return instance;
    }

    public List<Country> getAllCountries() {
        List<Country> countriesFound = new ArrayList<>();
        try (Connection connection = databaseService.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM country ORDER BY name");
            while (rs.next()) {
                countriesFound.add(
                        new Country(
                                rs.getString("code"),
                                rs.getString("name")
                        )
                );
            }
            databaseService.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countriesFound;
    }
}
