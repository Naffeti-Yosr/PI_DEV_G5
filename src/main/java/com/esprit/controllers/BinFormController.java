package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import com.esprit.models.Bin;
import com.esprit.services.BinService;
import javafx.stage.Stage;

public class BinFormController {
    @FXML private TextField Location;
    @FXML private TextField TypeDechet;
    @FXML private TextField Remplissage;
    @FXML private TextField Statut;

    private Bin binToEdit;
    private final BinService binService = new BinService();

    public void setBinData(Bin bin) {
        this.binToEdit = bin;
        Location.setText(bin.getLocation());
        TypeDechet.setText(bin.getTypeDechet());
        Remplissage.setText(String.valueOf(bin.getNiveauRemplissage()));
        Statut.setText(bin.getStatut());
    }

    @FXML
    private void handleAddButton() {
        try {
            // Validation des champs obligatoires
            if (!validateFields()) {
                return;
            }

            // Validation du niveau de remplissage
            double niveauRemplissage = Double.parseDouble(Remplissage.getText());
            if (niveauRemplissage < 0 || niveauRemplissage > 100) {
                showErrorAlert("Niveau de remplissage invalide", "Le niveau doit être entre 0 et 100%");
                highlightErrorField(Remplissage);
                return;
            }

            // Création ou mise à jour du bin
            if (binToEdit == null) {
                Bin newBin = new Bin(
                        Location.getText(),
                        TypeDechet.getText(),
                        niveauRemplissage,
                        Statut.getText()
                );
                binService.add(newBin);
                showSuccessAlert("Poubelle ajoutée", "La poubelle a été ajoutée avec succès");
            } else {
                binToEdit.setLocation(Location.getText());
                binToEdit.setTypeDechet(TypeDechet.getText());
                binToEdit.setNiveauRemplissage(niveauRemplissage);
                binToEdit.setStatut(Statut.getText());
                binService.update(binToEdit);
                showSuccessAlert("Poubelle modifiée", "La poubelle a été modifiée avec succès");
            }

            // Fermer la fenêtre
            Location.getScene().getWindow().hide();

        } catch (NumberFormatException e) {
            showErrorAlert("Format invalide", "Le niveau de remplissage doit être un nombre");
            highlightErrorField(Remplissage);
        } catch (Exception e) {
            showErrorAlert("Erreur", "Une erreur est survenue: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        boolean isValid = true;

        if (Location.getText().isEmpty()) {
            highlightErrorField(Location);
            isValid = false;
        }

        if (TypeDechet.getText().isEmpty()) {
            highlightErrorField(TypeDechet);
            isValid = false;
        }

        if (Remplissage.getText().isEmpty()) {
            highlightErrorField(Remplissage);
            isValid = false;
        }

        if (Statut.getText().isEmpty()) {
            highlightErrorField(Statut);
            isValid = false;
        }

        if (!isValid) {
            showErrorAlert("Champs manquants", "Veuillez remplir tous les champs obligatoires");
        }

        return isValid;
    }

    private void highlightErrorField(TextField field) {
        field.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
        field.requestFocus();

        // Retirer le style après un certain temps ou lors de la modification
        field.textProperty().addListener((obs, oldVal, newVal) -> {
            field.setStyle("");
        });
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleBackButton() {
        // Close the current window
        ((Stage) Location.getScene().getWindow()).close();
    }
}