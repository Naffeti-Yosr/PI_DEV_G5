package com.esprit.controllers;

import com.esprit.Services.EvenementService;
import com.esprit.Services.ReservationService;
import com.esprit.models.Evenement;
import com.esprit.models.Reservation;
import com.esprit.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDetailsController {

    @FXML private ImageView posterImage;
    @FXML private Label titleLabel;
    @FXML private Label dateLabel;
    @FXML private Label adresseLabel;
    @FXML private Label descriptionLabel;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Button backButton;
    @FXML private Button reserveButton;
    @FXML private VBox searchContainer;
    @FXML private VBox dayFilterContainer;

    private BorderPane mainContainer;
    private Evenement evenement;
    private User currentUser;

    private final EvenementService evenementService = new EvenementService();
    private final ReservationService reservationService = new ReservationService();
    private EventController eventController;

    public void setEventController(EventController controller) {
        this.eventController = controller;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setEvent(Evenement evt, BorderPane mainContainer, VBox searchContainer, VBox dayFilterContainer, User currentUser) {
        this.evenement = evt;
        this.mainContainer = mainContainer;
        this.searchContainer = searchContainer;
        this.dayFilterContainer = dayFilterContainer;
        this.currentUser = currentUser;

        // Affichage des données
        titleLabel.setText(evt.getTitre());
        dateLabel.setText("📅 " + evt.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        adresseLabel.setText("📍 " + evt.getAdresse());
        descriptionLabel.setText("📝 " + evt.getDescription());

        // Chargement de l'image
        String path = "/default_event.png";
        if (evt.getPoster() != null && evt.getPoster().getImagePath() != null) {
            path = evt.getPoster().getImagePath();
        }
        try {
            posterImage.setImage(new Image(getClass().getResource(path).toExternalForm()));
        } catch (Exception e) {
            posterImage.setImage(new Image(getClass().getResource("/default_event.png").toExternalForm()));
        }

        // Affichage bouton réservation selon rôle
        if (currentUser == null || !"ROLE_PARTICIPANT".equals(currentUser.getRole())) {
            reserveButton.setVisible(false);
            reserveButton.setManaged(false);
        } else {
            boolean alreadyReserved = reservationService.exists(currentUser.getId(), evt.getId());
            reserveButton.setDisable(alreadyReserved);
        }

        reserveButton.setOnAction(e -> handleReservation());
        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
        backButton.setOnAction(e -> handleBack());
    }

    private void handleReservation() {
        if (currentUser == null || !"ROLE_PARTICIPANT".equals(currentUser.getRole())) {
            showAlert("Erreur", "Vous devez être connecté en tant que participant pour réserver !");
            return;
        }

        if (reservationService.exists(currentUser.getId(), evenement.getId())) {
            showAlert("Info", "Vous avez déjà réservé cet événement.");
            return;
        }

        Reservation r = new Reservation(0, currentUser, evenement, LocalDateTime.now());
        reservationService.add(r);

        showAlert("Réservé !", "Votre réservation a été enregistrée.");
        reserveButton.setDisable(true);

        if (eventController != null) {
            eventController.reloadEvents();
        }
    }

    private void handleEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutEvent.fxml"));
            Parent form = loader.load();

            AjoutEvent controller = loader.getController();
            controller.setMode("edit");
            controller.setEvenementToEdit(evenement);
            controller.setMainContainer(mainContainer);
            controller.prefillFieldsIfEdit();

            mainContainer.setCenter(form);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cet événement ?", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Suppression de l’événement");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                evenementService.delete(evenement.getId());
                if (eventController != null) eventController.reloadEvents();
                handleBack();
            }
        });
    }

    private void handleBack() {
        try {
            searchContainer.setVisible(true);
            searchContainer.setManaged(true);
            dayFilterContainer.setVisible(true);
            dayFilterContainer.setManaged(true);
            mainContainer.setCenter(dayFilterContainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
