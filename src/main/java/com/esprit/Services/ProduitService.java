package com.esprit.Services;

import com.esprit.models.Produit;
import com.esprit.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProduitService implements IService<Produit> {

    private Connection connection;

    public ProduitService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(Produit produit) {
        String query = "INSERT INTO produit (nom, description, picture, price, categorie_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, produit.getNom());
            pst.setString(2, produit.getDescription());
            pst.setString(3, produit.getPicture());
            pst.setFloat(4, produit.getPrice());
            pst.setInt(5, produit.getCategorieId());
            pst.executeUpdate();
            System.out.println("Produit ajouté: " + produit);
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du produit: " + e.getMessage());
        }
    }

    @Override
    public void modifier(Produit produit) {
        String query = "UPDATE produit SET nom = ?, description = ?, picture = ?, price = ?, categorie_id = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, produit.getNom());
            pst.setString(2, produit.getDescription());
            pst.setString(3, produit.getPicture());
            pst.setFloat(4, produit.getPrice());
            pst.setInt(5, produit.getCategorieId());
            pst.setInt(6, produit.getId());
            pst.executeUpdate();
            System.out.println("Produit modifié: " + produit);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification du produit: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(Produit produit) {
        String query = "DELETE FROM produit WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, produit.getId());
            pst.executeUpdate();
            System.out.println("Produit supprimé: " + produit);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du produit: " + e.getMessage());
        }
    }

    @Override
    public List<Produit> recuperer() {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT * FROM produit";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Produit produit = new Produit(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("picture"),
                        rs.getFloat("price"),
                        rs.getInt("categorie_id")
                );
                produits.add(produit);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des produits: " + e.getMessage());
        }
        return produits;
    }

    public Produit trouverParId(int id) {
        String query = "SELECT * FROM produit WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Produit(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("description"),
                            rs.getString("picture"),
                            rs.getFloat("price"),
                            rs.getInt("categorie_id")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche du produit: " + e.getMessage());
        }
        return null;
    }
}