package com.esprit.Services;

import com.esprit.models.Commande;
import com.esprit.utils.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeService implements IService<Commande> {

    private final Connection connection;

    public CommandeService() {
        this.connection = DataSource.getInstance().getConnection();
    }

    @Override
    public void ajouter(Commande commande) {
        String query = "INSERT INTO Commande (dateCommande, statut, total, clientId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setDate(1, new Date(commande.getDate().getTime()));
            preparedStatement.setString(2, commande.getStatut());
            preparedStatement.setDouble(3, 0.0); // initial total 0
            preparedStatement.setInt(4, commande.getClientId());

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    commande.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la commande : " + e.getMessage());
        }
    }

    public void updateTotalForCommande(int commandeId) {
        String sumQuery = "SELECT SUM(sousTotal) AS totalSum FROM commande_produit WHERE commande_id = ?";
        String updateQuery = "UPDATE Commande SET total = ? WHERE id = ?";
        try (PreparedStatement sumStmt = connection.prepareStatement(sumQuery);
             PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
            sumStmt.setInt(1, commandeId);
            try (ResultSet rs = sumStmt.executeQuery()) {
                if (rs.next()) {
                    double totalSum = rs.getDouble("totalSum");
                    updateStmt.setDouble(1, totalSum);
                    updateStmt.setInt(2, commandeId);
                    updateStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du total de la commande : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Commande commande) {
        String query = "UPDATE Commande SET dateCommande = ?, statut = ?, clientId = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setDate(1, new Date(commande.getDate().getTime()));
            preparedStatement.setString(2, commande.getStatut());
            preparedStatement.setInt(3, commande.getClientId());
            preparedStatement.setInt(4, commande.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la modification de la commande : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(Commande commande) {
        String query = "DELETE FROM Commande WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, commande.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la commande : " + e.getMessage());
        }
    }

    @Override
    public List<Commande> recuperer() {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM Commande";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Commande commande = new Commande(
                        resultSet.getInt("id"),
                        resultSet.getDate("dateCommande"),
                        resultSet.getString("statut"),
                        resultSet.getInt("clientId")
                );
                commandes.add(commande);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des commandes : " + e.getMessage());
        }
        return commandes;
    }
}
