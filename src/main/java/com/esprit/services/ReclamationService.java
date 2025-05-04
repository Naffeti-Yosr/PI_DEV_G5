package com.esprit.services;

import com.esprit.models.Reclamation;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService {

    private Connection connection;

    public ReclamationService() {
        connection = DataSource.getInstance().getConnection();
    }

    public void addReclamation(Reclamation reclamation) throws SQLException {
        String query = "INSERT INTO Reclamation (user_id, report, status, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, reclamation.getUserId());
            stmt.setString(2, reclamation.getReport());
            stmt.setString(3, reclamation.getStatus());
            stmt.setTimestamp(4, new Timestamp(reclamation.getCreatedAt().getTime()));
            stmt.executeUpdate();
        }
    }

    public void updateReclamation(Reclamation reclamation) throws SQLException {
        String query = "UPDATE Reclamation SET report = ?, status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, reclamation.getReport());
            stmt.setString(2, reclamation.getStatus());
            stmt.setInt(3, reclamation.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteReclamation(int id) throws SQLException {
        String query = "DELETE FROM Reclamation WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Reclamation getReclamationById(int id) throws SQLException {
        String query = "SELECT * FROM Reclamation WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToReclamation(rs);
            }
        }
        return null;
    }

    public List<Reclamation> getAllReclamations() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT r.id, r.user_id, u.nom, u.prenom, r.report, r.status, r.created_at " +
                       "FROM Reclamation r " +
                       "JOIN users u ON r.user_id = u.id";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                reclamations.add(mapResultSetToReclamation(rs));
            }
        }
        return reclamations;
    }

    public List<Reclamation> getReclamationsByUserId(int userId) throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM Reclamation WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reclamations.add(mapResultSetToReclamation(rs));
            }
        }
        return reclamations;
    }

    private Reclamation mapResultSetToReclamation(ResultSet rs) throws SQLException {
        Reclamation reclamation = new Reclamation();
        reclamation.setId(rs.getInt("id"));
        reclamation.setUserId(rs.getInt("user_id"));
        reclamation.setNom(rs.getString("nom"));
        reclamation.setPrenom(rs.getString("prenom"));
        reclamation.setReport(rs.getString("report"));
        reclamation.setStatus(rs.getString("status"));
        reclamation.setCreatedAt(rs.getTimestamp("created_at"));
        return reclamation;
    }
}
