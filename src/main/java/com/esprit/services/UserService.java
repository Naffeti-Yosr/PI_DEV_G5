package com.esprit.services;

import com.esprit.models.User;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IService<User> {
    private Connection connection;

    public UserService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public List<User> get() {

        List<User> users = new ArrayList<>();

        String req = "SELECT * FROM users";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                users.add(
                        new User(rs.getInt("id"),
                                rs.getString("nom"),
                                rs.getString("prenom"),
                                rs.getDate("birth_date"),
                                rs.getString("email"), rs.getString("password"), rs.getString("role")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return users;
    }


    @Override
    public void add(User user) {
        String sql = "INSERT INTO users(nom, prenom,birth_date,email,password,role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setDate(3, new java.sql.Date(user.getBirth_date().getTime()));
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getRole());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting user failed, no rows affected.");
            }
            // Get the generated ID
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }
            System.out.println("User added!");
        } catch (SQLException e) {
            System.out.println("Error in add(): " + e.getMessage());
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users  SET nom= ?, prenom=?,birth_date=?,email=?,password=?,role=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setDate(3, new java.sql.Date(user.getBirth_date().getTime()));
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getRole());
            ps.setInt(7, user.getId());


            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Inserting user failed, no rows affected.");
            }

            System.out.println("User updated!");
        } catch (SQLException e) {
            System.out.println("Error in update user(): " + e.getMessage());
        }
    }


    @Override
    public void delete(User user) {
        String sql = "DELETE FROM users WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            ps.executeUpdate();
            System.out.println("User deleted!");
        } catch (SQLException e) {
            System.out.println("Error in delete(): " + e.getMessage());
        }


    }
}
