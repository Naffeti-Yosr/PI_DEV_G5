package com.esprit.Services;

import com.esprit.models.Evenement;
import com.esprit.models.User;
import com.esprit.utils.DataSource;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EvenementService implements IService<Evenement> {
    private final Connection connection;

    public EvenementService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void add(Evenement Evenement) {

        int organizerId = Evenement.getOrganisateur().getId();
        if (!userExists(organizerId)) {
            System.out.println("Organizer with id " + organizerId + " does not exist. Evenement not created.");
            return;
        }

        String insertEvenementSql = "INSERT INTO Evenements(name, description, dateTime, address, organizer) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(insertEvenementSql, Statement.RETURN_GENERATED_KEYS)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateTimeStr = Evenement.getDate() != null ? Evenement.getDate().format(formatter) : null;

            ps.setString(1, Evenement.getTitre());
            ps.setString(2, Evenement.getDescription());
            ps.setString(3, dateTimeStr);
            ps.setString(4, Evenement.getAdresse());
            ps.setInt(5, organizerId);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating Evenement failed, no rows affected.");
            }
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    Evenement.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating Evenement failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println("add() error: " + e.getMessage());
        }

        // Insert into join table only if participants exist.
        if (Evenement.getParticipants() != null && !Evenement.getParticipants().isEmpty()) {
            String insertJoinSql = "INSERT INTO Evenement_participants(Evenement_id, user_id) VALUES (?, ?)";
            try (PreparedStatement psJoin = connection.prepareStatement(insertJoinSql)) {
                for (User user : Evenement.getParticipants()) {
                    if (userExists(user.getId())) {
                        psJoin.setInt(1, Evenement.getId());
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
        System.out.println("Evenement added!");
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
    public void update(Evenement Evenement) {
        int organizerId = Evenement.getOrganisateur().getId();
        if (!userExists(organizerId)) {
            System.out.println("Organizer with id " + organizerId + " does not exist. Evenement not updated.");
            return;
        }

        String sql = "UPDATE Evenements SET name=?, description=?, dateTime=?, address=?, organizer=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ps.setString(1, Evenement.getTitre());
            ps.setString(2, Evenement.getDescription());
            ps.setString(3, Evenement.getDate() != null ? Evenement.getDate().format(formatter) : null);
            ps.setString(4, Evenement.getAdresse());
            ps.setInt(5, organizerId);
            ps.setInt(6, Evenement.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error updating Evenement: " + ex.getMessage());
        }
    }


   /* public void delete(Evenement Evenement) {
        String deleteJoinSql = "DELETE FROM Evenement_participants WHERE Evenement_id=?";
        try (PreparedStatement psJoin = connection.prepareStatement(deleteJoinSql)) {
            psJoin.setInt(1, Evenement.getId());
            psJoin.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error deleting Evenement participants: " + ex.getMessage());
        }

        String sql = "DELETE FROM Evenements WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, Evenement.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error deleting Evenement: " + ex.getMessage());
        }
    }*/
   @Override
    public void delete(int EvenementId) {
        String deleteJoinSql = "DELETE FROM Evenement_participants WHERE Evenement_id=?";
        try (PreparedStatement psJoin = connection.prepareStatement(deleteJoinSql)) {
            psJoin.setInt(1, EvenementId);
            psJoin.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error deleting Evenement participants: " + ex.getMessage());
        }

        String sql = "DELETE FROM Evenements WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1,EvenementId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error deleting Evenement: " + ex.getMessage());
        }
    }

    @Override
    public List<Evenement> get() {
        List<Evenement> Evenements = new ArrayList<>();
        String sql = "SELECT * FROM Evenements";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Evenement Evenement = new Evenement();
                Evenement.setId(rs.getInt("id"));
                Evenement.setTitre(rs.getString("name"));
                Evenement.setDescription(rs.getString("description"));
                Timestamp ts = rs.getTimestamp("dateTime");
                if (ts != null) {
                    Evenement.setDate(ts.toLocalDateTime());
                }
                Evenement.setAdresse(rs.getString("address"));
                // Retrieve the organizer id and create a User object.
                int organizerId = rs.getInt("organizer");
                User organizer = new User();
                organizer.setId(organizerId);
                Evenement.setOrganisateur(organizer);
                Evenement.setParticipants(getParticipants(Evenement.getId()));
                Evenements.add(Evenement);
            }
        } catch (SQLException ex) {
            System.out.println("Error retrieving Evenements: " + ex.getMessage());
        }
        return Evenements;
    }

    private List<User> getParticipants(int EvenementId) {
        List<User> participants = new ArrayList<>();
        String sql = "SELECT u.* FROM users u JOIN Evenement_participants ep ON u.id = ep.user_id WHERE ep.Evenement_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, EvenementId);
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

    public void addParticipant(int EvenementId, User user) {
        String sql = "INSERT INTO Evenement_participants(Evenement_id, user_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, EvenementId);
            ps.setInt(2, user.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("Error adding participant: " + ex.getMessage());
        }
    }
}