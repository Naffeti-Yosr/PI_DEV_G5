package com.esprit.controllers;

import com.esprit.Services.EvenementService;
import com.esprit.Services.UserService;
import com.esprit.models.Evenement;
import com.esprit.models.User;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AjoutEvent {

    @FXML private TextField titreField;
    @FXML private TextField descriptionField;
    @FXML private DatePicker datePicker;
    @FXML private TextField heureField;
    @FXML private TextField adresseField;
    @FXML private ComboBox<User> organisateurCombo;
    @FXML private Button submitButton;

    private String mode = "add";
    private Evenement currentEvenement;
    private BorderPane mainContainer;

    public void setMainContainer(BorderPane container) {
        this.mainContainer = container;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setEvenementToEdit(Evenement evt) {
        this.currentEvenement = evt;
    }

    @FXML
    public void initialize() {
        List<User> users = new UserService().getAllUsers();
        organisateurCombo.setItems(FXCollections.observableArrayList(users));

        organisateurCombo.setCellFactory(param -> new ListCell<>() {
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText((empty || user == null) ? null : user.getNom());
            }
        });

        organisateurCombo.setButtonCell(new ListCell<>() {
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText((empty || user == null) ? null : user.getNom());
            }
        });

        submitButton.setOnAction(e -> handleSubmit());
    }

    public void prefillFieldsIfEdit() {
        if ("edit".equals(mode) && currentEvenement != null) {
            titreField.setText(currentEvenement.getTitre());
            descriptionField.setText(currentEvenement.getDescription());
            datePicker.setValue(currentEvenement.getDate().toLocalDate());
            heureField.setText(currentEvenement.getDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            adresseField.setText(currentEvenement.getAdresse());
            organisateurCombo.setValue(currentEvenement.getOrganisateur());
            submitButton.setText("Modifier l'événement");
        }
    }

    private void handleSubmit() {
        String titre = titreField.getText();
        String description = descriptionField.getText();
        LocalDate date = datePicker.getValue();
        String heureStr = heureField.getText();
        String adresse = adresseField.getText();
        User organisateur = organisateurCombo.getValue();

        if (titre.isEmpty() || description.isEmpty() || date == null || heureStr.isEmpty() || adresse.isEmpty() || organisateur == null) {
            showAlert(Alert.AlertType.ERROR, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        LocalTime heure;
        try {
            heure = LocalTime.parse(heureStr, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Heure invalide", "Veuillez entrer une heure au format HH:mm");
            return;
        }

        LocalDateTime dateTime = LocalDateTime.of(date, heure);
        EvenementService service = new EvenementService();

        if ("edit".equals(mode) && currentEvenement != null) {
            currentEvenement.setTitre(titre);
            currentEvenement.setDescription(description);
            currentEvenement.setDate(dateTime);
            currentEvenement.setAdresse(adresse);
            currentEvenement.setOrganisateur(organisateur);
            service.update(currentEvenement);
        } else {
            Evenement evt = new Evenement(0, titre, description, dateTime, adresse, organisateur);
            service.add(evt);
        }

        retourVersListe();
    }

    private void retourVersListe() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventsView.fxml"));
            Parent listView = loader.load();
            mainContainer.setCenter(listView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
