package com.esprit.services;

import com.esprit.models.Truck;
import com.esprit.services.IService;
import com.esprit.utils.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TruckService implements IService<Truck> {

    private Connection connection;

    public TruckService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void add(Truck truck) {
        String req = "INSERT INTO truck(capaciteMax, niveauRemplissageActuel,section , statut) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setDouble(1, truck.getCapaciteMax());
            pst.setDouble(2, truck.getNiveauRemplissageActuel());
            pst.setString(3, truck.getStatut());
            pst.setString(4, truck.getSection());
            pst.executeUpdate();
            System.out.println("Truck ajouté !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Truck truck) {
        String req = "UPDATE truck SET capaciteMax=?, niveauRemplissageActuel=?, section=?, statut=? WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setDouble(1, truck.getCapaciteMax());
            pst.setDouble(2, truck.getNiveauRemplissageActuel());
            pst.setString(3, truck.getSection());
            pst.setString(4, truck.getStatut());
            pst.setInt(5, truck.getId());
            pst.executeUpdate();
            System.out.println("Truck modifié !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Truck truck) {
        String req = "DELETE FROM truck WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, truck.getId());
            pst.executeUpdate();
            System.out.println("SmartTruck supprimé !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Truck> get() {
        List<Truck> trucks = new ArrayList<>();
        String req = "SELECT * FROM truck";
        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                trucks.add(new Truck(
                        rs.getInt("id"),
                        rs.getDouble("capaciteMax"),
                        rs.getDouble("niveauRemplissageActuel"),
                        rs.getString("section"),
                        rs.getString("statut")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return trucks;
    }

    public List<Truck> getAll() {
        // Implémentez la logique pour récupérer tous les camions
        // Exemple basique (à adapter selon votre système de persistance)
        return List.of(
                new Truck(1000.0, 500.0, "Section A", "Actif"),
                new Truck(2000.0, 1500.0, "Section B", "En maintenance")
        );

    }

}
