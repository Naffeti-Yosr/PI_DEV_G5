package com.esprit.Services;

import com.esprit.models.Evenement;
import com.esprit.models.User;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EvenementService implements IService<Evenement>, IEventService {
    private final Connection connection;

    public EvenementService() {
        connection = DataSource.getInstance().getConnection();
    }

    public List<Evenement> getAll() {
        List<Evenement> list = new ArrayList<>();
        String query = "SELECT * FROM Evenements";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Evenement e = new Evenement();
                e.setId(rs.getInt("id"));
                e.setTitre(rs.getString("name"));
                e.setDescription(rs.getString("description"));
                e.setAdresse(rs.getString("address"));
                Timestamp ts = rs.getTimestamp("dateTime");
                if (ts != null) {
                    e.setDate(ts.toLocalDateTime());
                }
                int organizerId = rs.getInt("organizer");
                User organizer = new User();
                organizer.setId(organizerId);
                e.setOrganisateur(organizer);

                list.add(e);
            }
        } catch (Exception e) {
            System.out.println("getAll() error: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void add(Evenement evenement) {
        int organizerId = evenement.getOrganisateur().getId();
        if (!userExists(organizerId)) {
            System.out.println("Organizer with id " + organizerId + " does not exist. Evenement not created.");
            return;
        }

        String sql = "INSERT INTO Evenements(name, description, dateTime, address, organizer) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ps.setString(1, evenement.getTitre());
            ps.setString(2, evenement.getDescription());
            ps.setString(3, evenement.getDate() != null ? evenement.getDate().format(formatter) : null);
            ps.setString(4, evenement.getAdresse());
            ps.setInt(5, organizerId);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) throw new SQLException("Creating Evenement failed, no rows affected.");

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    evenement.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating Evenement failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            System.out.println("add() error: " + e.getMessage());
        }
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

    @Override
    public void update(Evenement evenement) {
        int organizerId = evenement.getOrganisateur().getId();
        if (!userExists(organizerId)) {
            System.out.println("Organizer with id " + organizerId + " does not exist. Evenement not updated.");
            return;
        }

        String sql = "UPDATE Evenements SET name=?, description=?, dateTime=?, address=?, organizer=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ps.setString(1, evenement.getTitre());
            ps.setString(2, evenement.getDescription());
            ps.setString(3, evenement.getDate() != null ? evenement.getDate().format(formatter) : null);
            ps.setString(4, evenement.getAdresse());
            ps.setInt(5, organizerId);
            ps.setInt(6, evenement.getId());

            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("update() error: " + ex.getMessage());
        }
    }

    @Override
    public void delete(int evenementId) {
        String deleteParticipants = "DELETE FROM Evenement_participants WHERE Evenement_id=?";
        String deleteEvent = "DELETE FROM Evenements WHERE id=?";
        try (PreparedStatement ps1 = connection.prepareStatement(deleteParticipants);
             PreparedStatement ps2 = connection.prepareStatement(deleteEvent)) {

            ps1.setInt(1, evenementId);
            ps1.executeUpdate();

            ps2.setInt(1, evenementId);
            ps2.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("delete() error: " + ex.getMessage());
        }
    }

    @Override
    public List<Evenement> get() {
        return getAll(); // même implémentation que getAll()
    }

    @Override
    public Evenement getEvent(int id) {
        String sql = "SELECT * FROM Evenements WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Evenement evenement = new Evenement();
                    evenement.setId(rs.getInt("id"));
                    evenement.setTitre(rs.getString("name"));
                    evenement.setDescription(rs.getString("description"));
                    Timestamp ts = rs.getTimestamp("dateTime");
                    if (ts != null) {
                        evenement.setDate(ts.toLocalDateTime());
                    }
                    evenement.setAdresse(rs.getString("address"));
                    User organizer = new User();
                    organizer.setId(rs.getInt("organizer"));
                    evenement.setOrganisateur(organizer);
                    evenement.setParticipants(getParticipants(id));
                    return evenement;
                }
            }
        } catch (SQLException ex) {
            System.out.println("getEvent() error: " + ex.getMessage());
        }
        return null;
    }

    private List<User> getParticipants(int eventId) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.* FROM users u JOIN Evenement_participants ep ON u.id = ep.user_id WHERE ep.Evenement_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("id"));
                    // Ajouter d'autres champs utilisateur si besoin
                    list.add(u);
                }
            }
        } catch (SQLException ex) {
            System.out.println("getParticipants() error: " + ex.getMessage());
        }
        return list;
    }

    public void addParticipant(int eventId, User user) {
        String sql = "INSERT INTO Evenement_participants(Evenement_id, user_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            ps.setInt(2, user.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("addParticipant() error: " + ex.getMessage());
        }
    }
}
