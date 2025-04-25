package com.esprit.Services;

import com.esprit.models.CommandeProduit;
import com.esprit.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommandeProduitService implements IService<CommandeProduit> {

    private Connection connection;

    public CommandeProduitService() {
        connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(CommandeProduit commandeProduit) {
        String query = "INSERT INTO commande_produit (commande_id, produit_id, quantite, sousTotal) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, commandeProduit.getCommandeId());
            pst.setInt(2, commandeProduit.getProduitId());
            pst.setInt(3, commandeProduit.getQuantite());
            pst.setDouble(4, commandeProduit.getSousTotal());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(CommandeProduit commandeProduit) {
        String query = "UPDATE commande_produit SET quantite = ?, sousTotal = ? WHERE commande_id = ? AND produit_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, commandeProduit.getQuantite());
            pst.setDouble(2, commandeProduit.getSousTotal());
            pst.setInt(3, commandeProduit.getCommandeId());
            pst.setInt(4, commandeProduit.getProduitId());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public void supprimer(CommandeProduit commandeProduit) {
        String query = "DELETE FROM commande_produit WHERE commande_id = ? AND produit_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, commandeProduit.getCommandeId());
            pst.setInt(2, commandeProduit.getProduitId());
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<CommandeProduit> recuperer() {
        List<CommandeProduit> commandeProduits = new ArrayList<>();
        String query = "SELECT * FROM commande_produit";
        try (PreparedStatement pst = connection.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                CommandeProduit cp = new CommandeProduit(
                        rs.getInt("id"),
                        rs.getInt("commande_id"),
                        rs.getInt("produit_id"),
                        rs.getInt("quantite"),
                        rs.getDouble("sousTotal")
                );
                commandeProduits.add(cp);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return commandeProduits;
    }

    public CommandeProduit trouverParIds(int commandeId, int produitId) {
        String query = "SELECT * FROM commande_produit WHERE commande_id = ? AND produit_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, commandeId);
            pst.setInt(2, produitId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new CommandeProduit(
                            rs.getInt("id"),
                            rs.getInt("commande_id"),
                            rs.getInt("produit_id"),
                            rs.getInt("quantite"),
                            rs.getDouble("sousTotal")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<CommandeProduit> getOrderItemsByCommandeId(int commandeId) {
        List<CommandeProduit> items = new ArrayList<>();
        String query = "SELECT * FROM commande_produit WHERE commande_id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, commandeId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    CommandeProduit cp = new CommandeProduit(
                            rs.getInt("id"),
                            rs.getInt("commande_id"),
                            rs.getInt("produit_id"),
                            rs.getInt("quantite"),
                            rs.getDouble("sousTotal")
                    );
                    items.add(cp);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching order items: " + e.getMessage());
        }
        return items;
    }
}
