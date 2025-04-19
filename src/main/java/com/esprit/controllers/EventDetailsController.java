package com.esprit.controllers;

import com.esprit.Services.EvenementService;
import com.esprit.models.Evenement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;

public class EventDetailsController {

    @FXML
    private ImageView posterImage;
    @FXML
    private Label titleLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label adresseLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button backButton;
    private BorderPane mainContainer;
    @FXML
    private VBox searchContainer;

    @FXML
    private VBox dayFilterContainer;


    private Evenement evenement;
    private final EvenementService service = new EvenementService();

    public void setEvent(Evenement evt, BorderPane mainContainer, VBox searchContainer, VBox dayFilterContainer) {
        this.evenement = evt;
        this.mainContainer = mainContainer;
        this.searchContainer = searchContainer;
        this.dayFilterContainer = dayFilterContainer;

        titleLabel.setText(evt.getTitre());
        dateLabel.setText("📅 " + evt.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        adresseLabel.setText("📍 " + evt.getAdresse());
        descriptionLabel.setText("📝 " + evt.getDescription());

        String path = "/default_event.png";
        if (evt.getPoster() != null && evt.getPoster().getImagePath() != null) {
            path = evt.getPoster().getImagePath();
        }

        try {
            posterImage.setImage(new Image(getClass().getResource(path).toExternalForm()));
        } catch (Exception e) {
            posterImage.setImage(new Image(getClass().getResource("/default_event.png").toExternalForm()));
        }

        editButton.setOnAction(e -> handleEdit());
        deleteButton.setOnAction(e -> handleDelete());
        backButton.setOnAction(e -> handleBack());
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
                service.delete(evenement.getId());
                handleBack();
            }
        });
        if (eventController != null) {
            eventController.reloadEvents(); // Mise à jour après suppression
        }

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

    private EventController eventController;

    public void setEventController(EventController controller) {
        this.eventController = controller;
    }



}