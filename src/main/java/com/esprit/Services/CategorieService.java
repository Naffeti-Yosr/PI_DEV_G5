package com.esprit.Services;

import com.esprit.models.Categorie;
import com.esprit.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategorieService implements IService<Categorie> {

    private Connection connection;

    public CategorieService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(Categorie categorie) {
        String query = "INSERT INTO categorie (nom) VALUES (?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, categorie.getNom());
            pst.executeUpdate();
            System.out.println("Categorie ajoutée: " + categorie);
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la catégorie: " + e.getMessage());
        }
    }

    @Override
    public void modifier(Categorie categorie) {
        String query = "UPDATE categorie SET nom = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, categorie.getNom());
            pst.setInt(2, categorie.getId());
            pst.executeUpdate();
            System.out.println("Categorie modifiée: " + categorie);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la catégorie: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(Categorie categorie) {
        String query = "DELETE FROM categorie WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, categorie.getId());
            pst.executeUpdate();
            System.out.println("Categorie supprimée: " + categorie);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la catégorie: " + e.getMessage());
        }
    }

    @Override
    public List<Categorie> recuperer() {
        List<Categorie> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Categorie categorie = new Categorie(
                        rs.getInt("id"),
                        rs.getString("nom")
                );
                categories.add(categorie);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des catégories: " + e.getMessage());
        }
        return categories;
    }

    // Additional method to find a category by ID
    public Categorie trouverParId(int id) {
        String query = "SELECT * FROM categorie WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Categorie(
                            rs.getInt("id"),
                            rs.getString("nom")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de la catégorie: " + e.getMessage());
        }
        return null;
    }
}