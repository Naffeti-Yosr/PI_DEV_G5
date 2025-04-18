package com.esprit.services;

import com.esprit.models.Bin;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BinService implements IService<Bin> {

    private Connection connection;

    public BinService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void add(Bin smartBin) {
        String req = "INSERT INTO bin(location, typeDechet, niveauRemplissage, statut) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, smartBin.getLocation());
            pst.setString(2, smartBin.getTypeDechet());
            pst.setDouble(3, smartBin.getNiveauRemplissage());
            pst.setString(4, smartBin.getStatut());
            pst.executeUpdate();
            System.out.println("SmartBin ajoutée !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Bin smartBin) {
        String req = "UPDATE bin SET location=?, typeDechet=?, niveauRemplissage=?, statut=? WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, smartBin.getLocation());
            pst.setString(2, smartBin.getTypeDechet());
            pst.setDouble(3, smartBin.getNiveauRemplissage());
            pst.setString(4, smartBin.getStatut());
            pst.setInt(5, smartBin.getId());
            pst.executeUpdate();
            System.out.println("SmartBin modifiée !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Bin smartBin) {
        String req = "DELETE FROM bin WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, smartBin.getId());
            pst.executeUpdate();
            System.out.println("SmartBin supprimée !");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Bin> get() {
        List<Bin> smartBins = new ArrayList<>();
        String req = "SELECT * FROM bin";
        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                smartBins.add(new Bin(
                        rs.getInt("id"),
                        rs.getString("location"),
                        rs.getString("typeDechet"),
                        rs.getDouble("niveauRemplissage"),
                        rs.getString("statut")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return smartBins;
    }
}
