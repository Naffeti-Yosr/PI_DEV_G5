package com.esprit.services;

import com.esprit.models.User;
import com.esprit.models.UserRole;
import com.esprit.models.UserStatus;
import com.esprit.utils.DataSource;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class UserService implements IService<User> {
    private final Connection connection;

    public UserService() {
        this.connection = DataSource.getInstance().getConnection();
    }

    @Override
    public List<User> get() {
        List<User> users = new ArrayList<>();
        String req = "SELECT * FROM users";

        try {
            // Test connection
            if (connection == null || connection.isClosed()) {
                System.err.println("Database connection is null or closed!");
                return users;
            }

            System.out.println("Database connection is valid, executing query: " + req);

            try (PreparedStatement pst = connection.prepareStatement(req)) {
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        try {
                            User user = mapResultSetToUser(rs);
                            // Debug output for each user
                            System.out.println("Mapped user - ID: " + user.getId() +
                                    ", Name: " + user.getNom() + " " + user.getPrenom() +
                                    ", Email: " + user.getEmail() +
                                    ", Role: " + user.getRole() +
                                    ", Status: " + user.getStatus());
                            users.add(user);
                        } catch (SQLException e) {
                            System.err.println("Error mapping user from ResultSet: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                }
            }

            System.out.println("Query completed. Found " + users.size() + " users");
            if (users.isEmpty()) {
                System.out.println("Warning: No users found in database!");
            }

        } catch (SQLException e) {
            System.err.println("Database error occurred!");
            System.err.println("Error Message: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
        return users;
    }

    public List<User> search(String keyword) {
        List<User> users = new ArrayList<>();
        String req = "SELECT * FROM users WHERE nom LIKE ? OR prenom LIKE ? OR email LIKE ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            String searchTerm = "%" + keyword + "%";
            ps.setString(1, searchTerm);
            ps.setString(2, searchTerm);
            ps.setString(3, searchTerm);

            System.out.println("Executing search with keyword: " + keyword);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    try {
                        users.add(mapResultSetToUser(rs));
                    } catch (SQLException e) {
                        System.err.println("Error mapping user from search ResultSet: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Found " + users.size() + " users matching search");

        } catch (SQLException e) {
            System.err.println("Error searching users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    public void addUserForRegistration(User user) {
        String sql = "INSERT INTO users (nom, prenom, birth_date, email, password, adresse, telephone, role, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            if (emailExists(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }

            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getNom());
                ps.setString(2, user.getPrenom());
                ps.setDate(3, new Date(user.getBirth_date().getTime()));
                ps.setString(4, user.getEmail());
                ps.setString(5, hashedPassword);
                ps.setString(6, user.getAddress());
                ps.setString(7, user.getPhoneNumber());
                ps.setString(8, user.getRole() != null ? user.getRole().toString() : "USER"); // default role USER
                ps.setString(9, UserStatus.NON_CONFIRMED.getValue()); // status non confirmed

                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                    }
                }

                System.out.println("User registered successfully with ID: " + user.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error in addUserForRegistration(): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to register user", e);
        }
    }

    public void addUserByAdmin(User user) {
        String sql = "INSERT INTO users (nom, prenom, birth_date, email, password, adresse, telephone, role, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            if (emailExists(user.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }

            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getNom());
                ps.setString(2, user.getPrenom());
                ps.setDate(3, new Date(user.getBirth_date().getTime()));
                ps.setString(4, user.getEmail());
                ps.setString(5, hashedPassword);
                ps.setString(6, user.getAddress());
                ps.setString(7, user.getPhoneNumber());
                ps.setString(8, user.getRole() != null ? user.getRole().toString() : null);
                ps.setString(9, user.getStatus() != null ? user.getStatus().getValue() : UserStatus.NON_CONFIRMED.getValue());

                int affectedRows = ps.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                    }
                }

                System.out.println("User added by admin successfully with ID: " + user.getId());
            }
        } catch (SQLException e) {
            System.err.println("Error in addUserByAdmin(): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to add user by admin", e);
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET nom=?, prenom=?, birth_date=?, email=?, adresse=?, telephone=?, role=?, status=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getPrenom());
            ps.setDate(3, new java.sql.Date(user.getBirth_date().getTime()));
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getPhoneNumber());
            ps.setString(7, user.getRole() != null ? user.getRole().toString() : null);
            ps.setString(8, user.getStatus() != null ? user.getStatus().toString() : null);
            ps.setInt(9, user.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }

            System.out.println("User updated successfully: ID=" + user.getId());
        } catch (SQLException e) {
            System.err.println("Error in update(): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to update user", e);
        }
    }

    @Override
    public void delete(User user) {
        String sql = "DELETE FROM users WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting user failed, no rows affected.");
            }
            System.out.println("User deleted successfully: ID=" + user.getId());
        } catch (SQLException e) {
            System.err.println("Error in delete(): " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public User authenticate(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (BCrypt.checkpw(password, storedHash)) {
                        return mapResultSetToUser(rs);
                    }
                }
            }
        }
        return null;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setNom(rs.getString("nom"));
        user.setPrenom(rs.getString("prenom"));
        user.setBirth_date(rs.getDate("birth_date"));
        user.setEmail(rs.getString("email"));
        user.setAddress(rs.getString("adresse")); // Note: DB column is "adresse"
        user.setPhoneNumber(rs.getString("telephone")); // Note: DB column is "telephone"
        user.setPassword(rs.getString("password"));
        String roleStr = rs.getString("role");
        if (roleStr != null) {
            try {
                user.setRole(UserRole.fromString(roleStr));
            } catch (IllegalArgumentException e) {
                System.err.println("Unknown role: " + roleStr + ". Setting role to null.");
                user.setRole(null);
            }
        } else {
            user.setRole(null);
        }

        String statusStr = rs.getString("status");
        if (statusStr != null) {
            try {
                user.setStatusFromString(statusStr);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid status value in database: " + statusStr);
                user.setStatus(UserStatus.NON_CONFIRMED); // Default status
            }
        } else {
            user.setStatus(UserStatus.NON_CONFIRMED); // Default status
        }

        return user;
    }

    public int getNewUsers() {
        String sql = "SELECT COUNT(*) FROM users WHERE status = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, UserStatus.NON_CONFIRMED.getValue());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting non-confirmed users count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public List<User> getRecentUsers() {
        List<User> nonConfirmedUsers = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE status = ? ORDER BY birth_date DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, UserStatus.NON_CONFIRMED.getValue());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    nonConfirmedUsers.add(mapResultSetToUser(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting non-confirmed users: " + e.getMessage());
            e.printStackTrace();
        }
        return nonConfirmedUsers;
    }

    public int getActiveUsers() {
        String sql = "SELECT COUNT(*) FROM users WHERE status = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, UserStatus.CONFIRMED.getValue());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting active users count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) FROM users";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting total users count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public void Rigstration(User user) {
        // This method appears incomplete and unused, consider removing it
    }

    public int getBannedUsers() {
        String sql = "SELECT COUNT(*) FROM users WHERE status = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, UserStatus.BANNED.getValue());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting banned users count: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
}