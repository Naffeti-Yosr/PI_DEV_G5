package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import com.esprit.models.Truck;
import com.esprit.services.TruckService;
import javafx.stage.Stage;

public class TruckFormController {
    @FXML private TextField TC;  // Capacité Max
    @FXML private TextField TST; // Niveau Remplissage
    @FXML private TextField TR;  // Section
    @FXML private TextField TS;  // Statut

    private Truck truckToEdit;
    private final TruckService truckService = new TruckService();

    public void setTruckData(Truck truck) {
        this.truckToEdit = truck;
        TC.setText(String.valueOf(truck.getCapaciteMax()));
        TST.setText(String.valueOf(truck.getNiveauRemplissageActuel()));
        TR.setText(truck.getSection());
        TS.setText(truck.getStatut());
    }

    @FXML
    private void handleAddButton() {
        try {
            // Validation des champs obligatoires
            if (!validateFields()) {
                return;
            }

            // Validation des nombres
            double capaciteMax = Double.parseDouble(TC.getText());
            double niveauRemplissage = Double.parseDouble(TST.getText());

            if (capaciteMax <= 0) {
                showErrorAlert("Capacité invalide", "La capacité maximale doit être positive");
                highlightErrorField(TC);
                return;
            }

            if (niveauRemplissage < 0 || niveauRemplissage > capaciteMax) {
                showErrorAlert("Remplissage invalide", "Le niveau de remplissage doit être entre 0 et " + capaciteMax);
                highlightErrorField(TST);
                return;
            }

            // Création ou mise à jour du camion
            String section = TR.getText();
            String statut = TS.getText();

            if (truckToEdit == null) {
                Truck newTruck = new Truck(capaciteMax, niveauRemplissage, section, statut);
                truckService.add(newTruck);
                showSuccessAlert("Camion ajouté", "Le camion a été ajouté avec succès");
            } else {
                truckToEdit.setCapaciteMax(capaciteMax);
                truckToEdit.setNiveauRemplissageActuel(niveauRemplissage);
                truckToEdit.setSection(section);
                truckToEdit.setStatut(statut);
                truckService.update(truckToEdit);
                showSuccessAlert("Camion modifié", "Le camion a été modifié avec succès");
            }

            // Fermer la fenêtre
            TC.getScene().getWindow().hide();

        } catch (NumberFormatException e) {
            showErrorAlert("Format invalide", "Les champs numériques doivent contenir des nombres valides");
        } catch (Exception e) {
            showErrorAlert("Erreur", "Une erreur est survenue: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        boolean isValid = true;

        if (TC.getText().isEmpty()) {
            highlightErrorField(TC);
            isValid = false;
        }

        if (TST.getText().isEmpty()) {
            highlightErrorField(TST);
            isValid = false;
        }

        if (TR.getText().isEmpty()) {
            highlightErrorField(TR);
            isValid = false;
        }

        if (TS.getText().isEmpty()) {
            highlightErrorField(TS);
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

        // Retirer le style après modification
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
        ((Stage) TC.getScene().getWindow()).close();
    }
}