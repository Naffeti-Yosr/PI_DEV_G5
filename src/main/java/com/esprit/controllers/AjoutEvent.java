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
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AjoutEvent {

    @FXML private TextField titreField;
    @FXML private TextField descriptionField;
    @FXML private DatePicker datePicker;
    @FXML private TextField heureField;
    @FXML private TextField adresseField;
    @FXML private ComboBox<User> organisateurCombo;
    @FXML private Button submitButton;
    @FXML private Button backButton;


    private String mode = "add";
    private Evenement currentEvenement;
    @FXML
    private BorderPane mainContainer;

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setEvenementToEdit(Evenement evenement) {
        this.currentEvenement = evenement;
    }

    public void setMainContainer(BorderPane mainContainer) {
        this.mainContainer = mainContainer;
    }

    @FXML
    public void initialize() {
        UserService userService = new UserService();
        List<User> users = userService.getAllUsers();
        organisateurCombo.setItems(FXCollections.observableArrayList(users));

        organisateurCombo.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : user.getNom());
            }
        });

        organisateurCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : user.getNom());
            }
        });

        if ("edit".equals(mode) && currentEvenement != null) {
            prefillFieldsIfEdit();
        }

        submitButton.setOnAction(event -> handleSubmit());
        backButton.setOnAction(e -> retourAEventsView());

    }

    private void handleSubmit() {
        EvenementService evenementService = new EvenementService();

        String titre = titreField.getText();
        String description = descriptionField.getText();
        LocalDate date = datePicker.getValue();
        String heureStr = heureField.getText();
        String adresse = adresseField.getText();
        User organisateur = organisateurCombo.getValue();

        if (titre.isEmpty() || description.isEmpty() || date == null || heureStr.isEmpty()
                || adresse.isEmpty() || organisateur == null) {
            showAlert(Alert.AlertType.ERROR, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        LocalTime heure;
        try {
            heure = LocalTime.parse(heureStr, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            showAlert(Alert.AlertType.ERROR, "Format invalide", "Veuillez entrer l'heure au format HH:mm.");
            return;
        }

        LocalDateTime dateTime = LocalDateTime.of(date, heure);

        if ("edit".equals(mode) && currentEvenement != null) {
            currentEvenement.setTitre(titre);
            currentEvenement.setDescription(description);
            currentEvenement.setDate(dateTime);
            currentEvenement.setAdresse(adresse);
            currentEvenement.setOrganisateur(organisateur);

            evenementService.update(currentEvenement);
            // ✅ attendre que l’utilisateur clique sur OK
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Événement modifié avec succès !");
            alert.showAndWait().ifPresent(btn -> retourAEventsView());
        } else {
            Evenement evenement = new Evenement(0, titre, description, dateTime, adresse, organisateur);
            evenementService.add(evenement);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Événement ajouté avec succès !");
            alert.showAndWait().ifPresent(btn -> retourAEventsView());
        }
        com.esprit.tests.App.getInstance().showEventsScreen();
    }

    public void prefillFieldsIfEdit() {
        if (currentEvenement != null) {
            titreField.setText(currentEvenement.getTitre());
            descriptionField.setText(currentEvenement.getDescription());
            if (currentEvenement.getDate() != null) {
                datePicker.setValue(currentEvenement.getDate().toLocalDate());
                heureField.setText(currentEvenement.getDate().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            }
            adresseField.setText(currentEvenement.getAdresse());
            organisateurCombo.setValue(currentEvenement.getOrganisateur());

            submitButton.setText("Modifier l'événement");
        }
    }

    private void retourAEventsView() {
        com.esprit.tests.App.getInstance().showEventsScreen();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}