package com.esprit.utils;

import com.esprit.models.User;

import java.sql.*;

public class DataSource {

    private Connection connection;
    private static DataSource instance;

    private final String URL = "jdbc:mysql://localhost:3306/renewable_energy_db_test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private final String USERNAME = "root";
    private final String PASSWORD = "";

    private DataSource() {
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to DB !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static DataSource getInstance() {
        if(instance == null)
            instance = new DataSource();
        return instance;
    }

    public static User getCurrentUser() {
        return null;
    }

    public Connection getConnection() {
        return connection;
    }
}
