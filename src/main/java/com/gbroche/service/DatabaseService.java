package com.gbroche.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseService {

    private static DatabaseService instance;
    private Connection connection;
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    private DatabaseService() {
        Dotenv env = Dotenv.load();
        this.dbUrl = env.get("DB_URL");
        this.dbUser = env.get("DB_USER");
        this.dbPassword = env.get("DB_PASSWORD");
    }

    public static synchronized DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    public boolean isConnectionOpen() {
        try {
            return connection != null && !connection.isClosed();
        } catch (Exception e) {
            return false;
        }
    }

    public void openConnection() throws SQLException {
        connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || !connection.isClosed()) {
            openConnection();
        }
        return connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
            connection = null;
        } catch (SQLException e) {
            System.err.println("Failed to close transaction; error:'" + e.getMessage() + "'");
        }
    }

    public void setAutoCommit(boolean mustBeAutomatic) throws SQLException {
        connection.setAutoCommit(mustBeAutomatic);
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.err.println("Failed to rollback transaction; error:'" + e.getMessage() + "'");
        }
    }

}
