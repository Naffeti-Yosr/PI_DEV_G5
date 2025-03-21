package com.esprit.Services;

import com.esprit.models.Commande;
import com.esprit.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommandeService implements IService<Commande> {

    private Connection connection;

    public CommandeService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(Commande commande) {
        String query = "INSERT INTO commande (utilisateur_id, total) VALUES (?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, commande.getUtilisateurId());
            pst.setFloat(2, commande.getTotal());
            pst.executeUpdate();
            System.out.println("Commande ajoutée: " + commande);
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la commande: " + e.getMessage());
        }
    }

    @Override
    public void modifier(Commande commande) {
        String query = "UPDATE commande SET utilisateur_id = ?, total = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, commande.getUtilisateurId());
            pst.setFloat(2, commande.getTotal());
            pst.setInt(3, commande.getId());
            pst.executeUpdate();
            System.out.println("Commande modifiée: " + commande);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la commande: " + e.getMessage());
        }
    }

    @Override
    public void supprimer(Commande commande) {
        String query = "DELETE FROM commande WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, commande.getId());
            pst.executeUpdate();
            System.out.println("Commande supprimée: " + commande);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la commande: " + e.getMessage());
        }
    }

    @Override
    public List<Commande> recuperer() {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM commande";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Commande commande = new Commande(
                        rs.getInt("id"),
                        rs.getInt("utilisateur_id"),
                        rs.getFloat("total")
                );
                commandes.add(commande);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commandes: " + e.getMessage());
        }
        return commandes;
    }

    // Additional method to find an order by ID
    public Commande trouverParId(int id) {
        String query = "SELECT * FROM commande WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Commande(
                            rs.getInt("id"),
                            rs.getInt("utilisateur_id"),
                            rs.getFloat("total")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la recherche de la commande: " + e.getMessage());
        }
        return null;
    }
}