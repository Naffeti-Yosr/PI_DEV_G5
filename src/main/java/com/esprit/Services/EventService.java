package com.esprit.Services;

import com.esprit.models.Event;
import com.esprit.models.User;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventService implements IService<Event> {
    private final Connection connection;

    public EventService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void add(Event event) {

        int organizerId = event.getOrganizer().getId();
        if (!userExists(organizerId)) {
            System.out.println("Organizer with id " + organizerId + " does not exist. Event not created.");
            return;
        }

        String insertEventSql = "INSERT INTO events(name, description, dateTime, address, organizer) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertEventSql, Statement.RETURN_GENERATED_KEYS)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateTimeStr = event.getDateTime() != null ? event.getDateTime().format(formatter) : null;

            ps.setString(1, event.getName());
            ps.setString(2, event.getDescription());
            ps.setString(3, dateTimeStr);
            ps.setString(4, event.getAddress());
            ps.setInt(5, organizerId);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating event failed, no rows affected.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    event.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating event failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println("add() error: " + e.getMessage());
        }

        // Insert into join table only if participants exist.
        if (event.getParticipants() != null && !event.getParticipants().isEmpty()) {
            String insertJoinSql = "INSERT INTO event_participants(event_id, user_id) VALUES (?, ?)";
            try (PreparedStatement psJoin = connection.prepareStatement(insertJoinSql)) {
                for (User user : event.getParticipants()) {
                    if (userExists(user.getId())) {
                        psJoin.setInt(1, event.getId());
                        psJoin.setInt(2, user.getId());
                        psJoin.executeUpdate();
                    } else {
                        System.out.println("User with id " + user.getId() + " does not exist in the users table.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("add() join table error: " + e.getMessage());
            }
        }
        System.out.println("Event added!");
    }

    // Helper method to check if a user exists in the 'users' table.
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

    @Override
    public void update(Event event) {
        // Check if organizer exists
        int organizerId = event.getOrganizer().getId();
        if (!userExists(organizerId)) {
            System.out.println("Organizer with id " + organizerId + " does not exist. Event not updated.");
            return;
        }

        String sql = "UPDATE events SET name=?, description=?, dateTime=?, address=?, organizer=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ps.setString(1, event.getName());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getDateTime() != null ? event.getDateTime().format(formatter) : null);
            ps.setString(4, event.getAddress());
            ps.setInt(5, organizerId);
            ps.setInt(6, event.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error updating event: " + ex.getMessage());
        }
    }

    @Override
    public void delete(Event event) {
        String deleteJoinSql = "DELETE FROM event_participants WHERE event_id=?";
        try (PreparedStatement psJoin = connection.prepareStatement(deleteJoinSql)) {
            psJoin.setInt(1, event.getId());
            psJoin.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error deleting event participants: " + ex.getMessage());
        }

        // Then, delete the event record
        String sql = "DELETE FROM events WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, event.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error deleting event: " + ex.getMessage());
        }
    }

    @Override
    public List<Event> get() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getInt("id"));
                event.setName(rs.getString("name"));
                event.setDescription(rs.getString("description"));
                Timestamp ts = rs.getTimestamp("dateTime");
                if (ts != null) {
                    event.setDateTime(ts.toLocalDateTime());
                }
                event.setAddress(rs.getString("address"));
                // Retrieve the organizer id and create a User object.
                int organizerId = rs.getInt("organizer");
                User organizer = new User();
                organizer.setId(organizerId);
                event.setOrganizer(organizer);
                event.setParticipants(getParticipants(event.getId()));
                events.add(event);
            }
        } catch (SQLException ex) {
            System.out.println("Error retrieving events: " + ex.getMessage());
        }
        return events;
    }

    private List<User> getParticipants(int eventId) {
        List<User> participants = new ArrayList<>();
        String sql = "SELECT u.* FROM users u JOIN event_participants ep ON u.id = ep.user_id WHERE ep.event_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    // Set additional user fields as needed.
                    participants.add(user);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error retrieving participants: " + ex.getMessage());
        }
        return participants;
    }

    // Method to let a user join an existing event.
    public void addParticipant(int eventId, User user) {
        String sql = "INSERT INTO event_participants(event_id, user_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            ps.setInt(2, user.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error adding participant: " + ex.getMessage());
        }
    }
}