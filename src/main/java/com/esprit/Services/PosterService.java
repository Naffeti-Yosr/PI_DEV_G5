package com.esprit.Services;

import com.esprit.models.Poster;
import com.esprit.models.User;
import com.esprit.models.Event;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class PosterService implements IService<Poster> {
    private final Connection connection;

    public PosterService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void add(Poster poster) {
        int createdById = poster.getCreatedBy().getId();
        int eventId = poster.getEvent().getId();

        if (!userExists(createdById)) {
            System.out.println("User with id " + createdById + " does not exist. Poster not created.");
            return;
        }

        if (!eventExists(eventId)) {
            System.out.println("Event with id " + eventId + " does not exist. Poster not created.");
            return;
        }

        String sql = "INSERT INTO posters(title, description, imageUrl, createdAt, event, createdBy) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String createdAtStr = poster.getCreatedAt() != null ? poster.getCreatedAt().format(formatter) : null;

            ps.setString(1, poster.getTitle());
            ps.setString(2, poster.getDescription());
            ps.setString(3, poster.getImageUrl());
            ps.setString(4, createdAtStr);
            ps.setInt(5, eventId);
            ps.setInt(6, createdById);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating poster failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    poster.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating poster failed, no ID obtained.");
                }
            }

            System.out.println("Poster added!");
        } catch (SQLException e) {
            System.out.println("add() error: " + e.getMessage());
        }
    }

    @Override
    public void update(Poster poster) {
        int createdById = poster.getCreatedBy().getId();
        int eventId = poster.getEvent().getId();

        if (!userExists(createdById)) {
            System.out.println("User with id " + createdById + " does not exist. Poster not updated.");
            return;
        }

        if (!eventExists(eventId)) {
            System.out.println("Event with id " + eventId + " does not exist. Poster not updated.");
            return;
        }

        String sql = "UPDATE posters SET title=?, description=?, imageUrl=?, createdAt=?, event=?, createdBy=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String createdAtStr = poster.getCreatedAt() != null ? poster.getCreatedAt().format(formatter) : null;

            ps.setString(1, poster.getTitle());
            ps.setString(2, poster.getDescription());
            ps.setString(3, poster.getImageUrl());
            ps.setString(4, createdAtStr);
            ps.setInt(5, eventId);
            ps.setInt(6, createdById);
            ps.setInt(7, poster.getId());

            ps.executeUpdate();
            System.out.println("Poster updated!");
        } catch (SQLException e) {
            System.out.println("update() error: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM posters WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Poster with ID " + id + " deleted.");
        } catch (SQLException ex) {
            System.out.println("Error deleting poster by ID: " + ex.getMessage());
        }
    }

    @Override
    public List<Poster> get() {
        List<Poster> posters = new ArrayList<>();
        String sql = "SELECT * FROM posters";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Poster poster = new Poster();
                poster.setId(rs.getInt("id"));
                poster.setTitle(rs.getString("title"));
                poster.setDescription(rs.getString("description"));
                poster.setImageUrl(rs.getString("imageUrl"));

                Timestamp ts = rs.getTimestamp("createdAt");
                if (ts != null) {
                    poster.setCreatedAt(ts.toLocalDateTime());
                }

                int createdById = rs.getInt("createdBy");
                User user = new User();
                user.setId(createdById);
                poster.setCreatedBy(user);

                int eventId = rs.getInt("event");
                Event event = new Event();
                event.setId(eventId);
                poster.setEvent(event);

                posters.add(poster);
            }

        } catch (SQLException e) {
            System.out.println("get() error: " + e.getMessage());
        }

        return posters;
    }

    private boolean userExists(int userId) {
        String sql = "SELECT id FROM users WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("userExists() error: " + e.getMessage());
            return false;
        }
    }

    private boolean eventExists(int eventId) {
        String sql = "SELECT id FROM events WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("eventExists() error: " + e.getMessage());
            return false;
        }
    }
}
