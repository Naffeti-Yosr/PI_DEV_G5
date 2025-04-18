package com.esprit.Services;

import com.esprit.models.Users;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersService {

    private final Connection connection;

    public UsersService() {
        this.connection = DataSource.getInstance().getConnection();
    }

    public List<Users> recuperer() {
        List<Users> users = new ArrayList<>();
        String query = "SELECT * FROM Users";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Users user = new Users(
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
