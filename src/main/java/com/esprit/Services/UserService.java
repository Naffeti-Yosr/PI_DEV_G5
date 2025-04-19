package com.esprit.Services;

import com.esprit.models.User;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final Connection connection;

    public UserService() {
        this.connection = DataSource.getInstance().getConnection();
    }

    public List<User> recuperer() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM User";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("prenom"),
                        resultSet.getTimestamp("birthDate").toLocalDateTime(),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("role")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
        return users;
    }
}
