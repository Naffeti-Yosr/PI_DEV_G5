package com.esprit.services;

import com.esprit.models.Client;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService implements IService<Client> {

    private Connection connection;

    public ClientService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public List<Client> get() {
        List<Client> clients = new ArrayList<>();
        String req = "SELECT * FROM client";

        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                clients.add(new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getDate("birth_date"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("address"),
                        rs.getString("phoneNumber")
                ));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching clients: " + e.getMessage());
        }

        return clients;
    }

    @Override
    public void add(Client client) {
        String sql = "INSERT INTO client(nom, prenom, birth_date, email, password, role, address, phoneNumber) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setDate(3, new java.sql.Date(client.getBirth_date().getTime()));
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getPassword());
            ps.setString(6, client.getRole());
            ps.setString(7, client.getAddress());
            ps.setString(8, client.getPhoneNumber());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Adding client failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    client.setId(rs.getInt(1));
                }
            }

            System.out.println("Client added!");
        } catch (SQLException e) {
            System.out.println("Error adding client: " + e.getMessage());
        }
    }

    @Override
    public void update(Client client) {
        String sql = "UPDATE client SET nom=?, prenom=?, birth_date=?, email=?, password=?, role=?, address=?, phoneNumber=? WHERE id=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, client.getNom());
            ps.setString(2, client.getPrenom());
            ps.setDate(3, new java.sql.Date(client.getBirth_date().getTime()));
            ps.setString(4, client.getEmail());
            ps.setString(5, client.getPassword());
            ps.setString(6, client.getRole());
            ps.setString(7, client.getAddress());
            ps.setString(8, client.getPhoneNumber());
            ps.setInt(9, client.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating client failed, no rows affected.");
            }

            System.out.println("Client updated!");
        } catch (SQLException e) {
            System.out.println("Error updating client: " + e.getMessage());
        }
    }

    @Override
    public void delete(Client client) {
        String sql = "DELETE FROM client WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, client.getId());
            ps.executeUpdate();
            System.out.println("Client deleted!");
        } catch (SQLException e) {
            System.out.println("Error deleting client: " + e.getMessage());
        }
    }
}
