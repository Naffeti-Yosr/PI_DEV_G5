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
        try (Connection con = DataSource.getInstance().getConnection()) {
            String query = "SELECT * FROM evenement";
            PreparedStatement stmt = con.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Evenement e = new Evenement();
                e.setId(rs.getInt("id"));
                e.setTitre(rs.getString("titre"));
                e.setDescription(rs.getString("description"));
                e.setAdresse(rs.getString("adresse"));
                e.setDate(rs.getTimestamp("date").toLocalDateTime());
                list.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void add(Evenement evenement) {
        int organizerId = evenement.getOrganisateur().getId();
        if (!userExists(organizerId)) {
            System.out.println("Organizer with id " + organizerId + " does not exist.");
            return;
        }

        String sql = "INSERT INTO Evenements(name, description, dateTime, address, organizer) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateTimeStr = evenement.getDate() != null ? evenement.getDate().format(formatter) : null;

            ps.setString(1, evenement.getTitre());
            ps.setString(2, evenement.getDescription());
            ps.setString(3, dateTimeStr);
            ps.setString(4, evenement.getAdresse());
            ps.setInt(5, organizerId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating event failed, no rows affected.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    evenement.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erreur ajout événement : " + e.getMessage());
        }

        if (evenement.getParticipants() != null && !evenement.getParticipants().isEmpty()) {
            String joinSql = "INSERT INTO Evenement_participants(Evenement_id, user_id) VALUES (?, ?)";
            try (PreparedStatement psJoin = connection.prepareStatement(joinSql)) {
                for (User user : evenement.getParticipants()) {
                    if (userExists(user.getId())) {
                        psJoin.setInt(1, evenement.getId());
                        psJoin.setInt(2, user.getId());
                        psJoin.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                System.out.println("Erreur insertion participants : " + e.getMessage());
            }
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
            System.out.println("Erreur vérification utilisateur : " + e.getMessage());
            return false;
        }
    }

    @Override
    public void update(Evenement evenement) {
        int organizerId = evenement.getOrganisateur().getId();
        if (!userExists(organizerId)) {
            System.out.println("Organizer with id " + organizerId + " does not exist.");
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
        } catch (SQLException e) {
            System.out.println("Erreur modification : " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String deleteParticipants = "DELETE FROM Evenement_participants WHERE Evenement_id=?";
        String deleteEvent = "DELETE FROM Evenements WHERE id=?";

        try (PreparedStatement ps1 = connection.prepareStatement(deleteParticipants)) {
            ps1.setInt(1, id);
            ps1.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur suppression participants : " + e.getMessage());
        }

        try (PreparedStatement ps2 = connection.prepareStatement(deleteEvent)) {
            ps2.setInt(1, id);
            ps2.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur suppression événement : " + e.getMessage());
        }
    }

    @Override
    public List<Evenement> get() {
        List<Evenement> list = new ArrayList<>();
        String sql = "SELECT * FROM Evenements";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Evenement e = new Evenement();
                e.setId(rs.getInt("id"));
                e.setTitre(rs.getString("name"));
                e.setDescription(rs.getString("description"));
                Timestamp ts = rs.getTimestamp("dateTime");
                if (ts != null) {
                    e.setDate(ts.toLocalDateTime());
                }
                e.setAdresse(rs.getString("address"));

                User org = new User();
                org.setId(rs.getInt("organizer"));
                e.setOrganisateur(org);

                e.setParticipants(getParticipants(e.getId()));
                list.add(e);
            }

        } catch (SQLException e) {
            System.out.println("Erreur chargement événements : " + e.getMessage());
        }

        return list;
    }

    @Override
    public Evenement getEvent(int id) {
        Evenement e = new Evenement();
        String sql = "SELECT * FROM Evenements WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    e.setId(rs.getInt("id"));
                    e.setTitre(rs.getString("name"));
                    e.setDescription(rs.getString("description"));
                    Timestamp ts = rs.getTimestamp("dateTime");
                    if (ts != null) {
                        e.setDate(ts.toLocalDateTime());
                    }
                    e.setAdresse(rs.getString("address"));
                    User org = new User();
                    org.setId(rs.getInt("organizer"));
                    e.setOrganisateur(org);
                    e.setParticipants(getParticipants(id));
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erreur récupération événement : " + ex.getMessage());
        }
        return e;
    }

    private List<User> getParticipants(int eventId) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT u.* FROM users u JOIN Evenement_participants ep ON u.id = ep.user_id WHERE ep.Evenement_id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    list.add(user);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur participants : " + e.getMessage());
        }
        return list;
    }

    public void addParticipant(int eventId, User user) {
        String sql = "INSERT INTO Evenement_participants(Evenement_id, user_id) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            ps.setInt(2, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur ajout participant : " + e.getMessage());
        }
    }
}
