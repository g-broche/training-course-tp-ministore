package com.gbroche.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gbroche.model.City;
import com.gbroche.service.DatabaseService;

/**
 * Manages requests involving cities in the database
 */
public class CityDao {

    private static CityDao instance;
    private final DatabaseService databaseService;

    private CityDao(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public static synchronized CityDao getInstance() {
        if (instance == null) {
            instance = new CityDao(DatabaseService.getInstance());
        }
        return instance;
    }

    /**
     * Return all cities belonging to a certain country by matching the country code
     * 
     * @param countryCode
     * @return list of cities
     */
    public List<City> getCitiesByCountryCode(String countryCode) {
        List<City> citiesFound = new ArrayList<>();
        try (Connection connection = databaseService.getConnection()) {
            String query = "SELECT * FROM City WHERE countrycode=? ORDER BY name";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, countryCode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                citiesFound.add(
                        new City(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("countrycode")));
            }
            databaseService.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return citiesFound;
    }
}
