package com.esprit.controllers;

import com.esprit.Services.EvenementService;
import com.esprit.models.Evenement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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

    private VBox mainContainer;
    private VBox searchContainer;
    private Evenement evenement;
    private final EvenementService service = new EvenementService();

    public void setEvent(Evenement evt, VBox mainContainer, VBox searchContainer) {
        this.evenement = evt;
        this.mainContainer = mainContainer;
        this.searchContainer = searchContainer;

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
            controller.prefillFieldsIfEdit();

            mainContainer.getChildren().set(1, form);
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
    }

    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventsView.fxml"));
            Parent listView = loader.load();

            mainContainer.getChildren().setAll(listView);
            searchContainer.setVisible(true);
            searchContainer.setManaged(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}