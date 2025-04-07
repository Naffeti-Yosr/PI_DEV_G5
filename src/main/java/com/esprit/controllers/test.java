package com.esprit.controllers;

import com.esprit.models.Bin;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class test {

    @FXML
    private TextField Location;

    @FXML
    private TextField Remplissage;

    @FXML
    private TextField Statut;

    @FXML
    private TextField TypeDechet; // Restauré en TextField

    @FXML
    private AnchorPane mainPane;

    @FXML
    private void handleAddButton() {
        try {
            // Validation
            if (Location.getText().trim().isEmpty() ||
                    TypeDechet.getText().trim().isEmpty() ||
                    Remplissage.getText().trim().isEmpty() ||
                    Statut.getText().trim().isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis");
                return;
            }

            // Création de la poubelle
            Bin newBin = new Bin(
                    Location.getText().trim(),
                    TypeDechet.getText().trim(), // Utilisation comme TextField
                    Double.parseDouble(Remplissage.getText().trim()),
                    Statut.getText().trim()
            );

            // À remplacer par votre service
            System.out.println("DEBUG: " + newBin.toString());

            showAlert("Succès", "SmartBin ajoutée !");
            resetForm();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le niveau de remplissage doit être un nombre (ex: 75.5)");
        }
    }

    private void resetForm() {
        Location.clear();
        TypeDechet.clear();
        Remplissage.clear();
        Statut.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


