package com.esprit.Services;

import com.esprit.models.Evenement;
import com.esprit.models.Reservation;
import com.esprit.models.User;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    private final Connection connection;

    public ReservationService() {
        connection = DataSource.getInstance().getConnection();
    }

    public void add(Reservation reservation) {
        String sql = "INSERT INTO reservations (user_id, event_id, date_reservation) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, reservation.getUser().getId());
            statement.setInt(2, reservation.getEvent().getId());
            statement.setTimestamp(3, Timestamp.valueOf(reservation.getDateReservation()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Reservation> getByEvent(int eventId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations WHERE event_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, eventId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setId(rs.getInt("id"));

                User user = new User();
                user.setId(rs.getInt("user_id"));
                reservation.setUser(user);

                Evenement event = new Evenement();
                event.setId(rs.getInt("event_id"));
                reservation.setEvent(event);

                reservation.setDateReservation(rs.getTimestamp("date_reservation").toLocalDateTime());
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    public boolean exists(int userId, int eventId) {
        String sql = "SELECT COUNT(*) FROM reservations WHERE user_id = ? AND event_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.setInt(2, eventId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
