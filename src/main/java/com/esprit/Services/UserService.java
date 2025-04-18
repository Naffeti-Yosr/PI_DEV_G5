package com.esprit.Services;

import com.esprit.models.User;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final Connection connection;

    public UserService() {
        connection = DataSource.getInstance().getConnection();
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, nom FROM users";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNom(rs.getString("nom")); // Assure-toi que le champ s'appelle bien "nom"
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("getAllUsers() error: " + e.getMessage());
        }

        return users;
    }
}
