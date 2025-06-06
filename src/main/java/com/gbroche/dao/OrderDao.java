package com.gbroche.dao;

import java.util.List;

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

    public boolean getCitiesByCountryCode(List<OrderLine> orderLines) {
        try {
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
