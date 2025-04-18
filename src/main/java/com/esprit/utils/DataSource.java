package com.esprit.utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class DataSource {

    private Connection connection;
    private static DataSource instance;

    private final String URL = "jdbc:mysql://127.0.0.1:3306/hi";
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

    public Connection getConnection() {
        if (connection == null) {
            getInstance();
        }
        return connection;
    }

    public Map<Integer, String> getProductImagesWithIds() {
        Map<Integer, String> productImages = new HashMap<>();
        String query = "SELECT id, image FROM Produit";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int productId = resultSet.getInt("id");
                String imagePath = resultSet.getString("image");
                
                // Convert absolute paths to relative if needed
                if (imagePath != null && imagePath.startsWith("C:")) {
                    imagePath = convertToRelativePath(imagePath);
                }
                
                productImages.put(productId, imagePath);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving product images: " + e.getMessage());
        }
        return productImages;
    }
    
    private String convertToRelativePath(String absolutePath) {
        // Extract just the filename from absolute path
        return absolutePath.substring(absolutePath.lastIndexOf("\\") + 1);
    }
}
