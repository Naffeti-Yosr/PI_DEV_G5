package com.esprit.controllers;

import com.esprit.models.Truck;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;

import java.awt.*;

public class test2 {

    @FXML
    private TextField TC;

    @FXML
    private TextField TR;

    @FXML
    private TextField TS;

    @FXML
    private TextField TST;
    @FXML
    private AnchorPane mainPane;

    @FXML
    private void handleAddButton() {
        try {
            // Validation
            if (TC.getText().trim().isEmpty() ||
                    TR.getText().trim().isEmpty() ||
                    TS.getText().trim().isEmpty() ||
                    TST.getText().trim().isEmpty()) {
                showAlert("Erreur", "Tous les champs doivent être remplis");
                return;
            }

            // Création du camion
            Truck newTruck = new Truck(
                    Double.parseDouble(TC.getText().trim()),    // capaciteMax
                    0.0,                                       // niveauRemplissageActuel (valeur par défaut)
                    TR.getText().trim(),                       // section
                    TS.getText().trim()                        // statut
            );

            // À remplacer par votre service
            System.out.println("DEBUG: " + newTruck.toString());

            showAlert("Succès", "Truck Smart ajouté !");
            resetForm();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "La capacité doit être un nombre (ex: 1000.0)");
        }
    }

    private void resetForm() {
        TC.clear();
        TR.clear();
        TS.clear();
        TST.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


