package com.esprit.Services;

import com.esprit.models.Poster;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class PosterService implements IService<Poster> {
    private final Connection connection;

    public PosterService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void add(Poster poster) {
        String sql = "INSERT INTO poster (title, description, imageUrl, createdAt, evenementId, createdById) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, poster.getTitre());
            ps.setString(2, poster.getDescription());
            ps.setString(3, poster.getImageUrl());
            ps.setTimestamp(4, Timestamp.valueOf(poster.getCreatedAt()));
            ps.setInt(5, poster.getEvenementId());
            ps.setInt(6, poster.getCreatedById());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de la création du poster");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    poster.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur add(): " + e.getMessage());
        }
    }

    @Override
    public void update(Poster poster) {
        String sql = "UPDATE poster SET title=?, description=?, imageUrl=?, createdAt=?, evenementId=?, createdById=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, poster.getTitre());
            ps.setString(2, poster.getDescription());
            ps.setString(3, poster.getImageUrl());
            ps.setTimestamp(4, Timestamp.valueOf(poster.getCreatedAt()));
            ps.setInt(5, poster.getEvenementId());
            ps.setInt(6, poster.getCreatedById());
            ps.setInt(7, poster.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur update(): " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM poster WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur delete(): " + e.getMessage());
        }
    }

    @Override
    public List<Poster> get() {
        List<Poster> posters = new ArrayList<>();
        String sql = "SELECT * FROM poster";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Poster poster = new Poster();
                poster.setId(rs.getInt("id"));
                poster.setTitre(rs.getString("title"));
                poster.setDescription(rs.getString("description"));
                poster.setImageUrl(rs.getString("imageUrl"));
                poster.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
                poster.setEvenementId(rs.getInt("evenementId"));
                poster.setCreatedById(rs.getInt("createdById"));

                posters.add(poster);
            }
        } catch (SQLException e) {
            System.err.println("Erreur get(): " + e.getMessage());
        }
        return posters;
    }



    private boolean userExists(int userId) {
        String sql = "SELECT id FROM users WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Erreur userExists(): " + e.getMessage());
            return false;
        }
    }

    private boolean eventExists(int eventId) {
        String sql = "SELECT id FROM evenement WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            System.err.println("Erreur eventExists(): " + e.getMessage());
            return false;
        }
    }
}