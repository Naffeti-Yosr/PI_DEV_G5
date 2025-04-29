package com.esprit.utils;

import java.sql.*;

public class DataSource {

    private Connection connection;
    private static DataSource instance;

    private final String URL = "jdbc:mysql://localhost:3306/renewable_energy_db";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    private DataSource() {
        try {
            // Test if MySQL driver is available
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Try to connect
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Successfully connected to database!");
            
            // Validate connection and table existence
            validateDatabase();
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection error!");
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
    }

    private void validateDatabase() {
        if (connection != null) {
            try {
                // Check if users table exists
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet tables = metaData.getTables(null, null, "users", null);
                
                if (!tables.next()) {
                    // Table doesn't exist, create it
                    createUsersTable();
                } else {
                    // Table exists, validate schema
                    System.out.println("Users table found, validating schema...");
                    validateTableSchema();
                }
                
            } catch (SQLException e) {
                System.err.println("Error validating database schema!");
                e.printStackTrace();
            }
        }
    }

    private void createUsersTable() {
        String createTableSQL = """
            CREATE TABLE users (
                id INT PRIMARY KEY AUTO_INCREMENT,
                nom VARCHAR(50),
                prenom VARCHAR(50),
                birth_date DATE,
                email VARCHAR(100) UNIQUE,
                password VARCHAR(255),
                adresse VARCHAR(255),
                telephone VARCHAR(20),
                role VARCHAR(50),
                status VARCHAR(20) DEFAULT 'non_confirmed'
            )
        """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("Users table created successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating users table!");
            e.printStackTrace();
        }
    }

    private void validateTableSchema() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, "users", null);
            
            // Print column information for debugging
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");
                System.out.println("Column: " + columnName + " (Type: " + columnType + ")");
            }
        } catch (SQLException e) {
            System.err.println("Error validating table schema!");
            e.printStackTrace();
        }
    }

    public static DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // Validate connection before returning
            if (connection == null || connection.isClosed()) {
                System.out.println("Connection is null or closed, reconnecting...");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            System.err.println("Error validating/reconnecting to database!");
            e.printStackTrace();
        }
        return connection;
    }
}
