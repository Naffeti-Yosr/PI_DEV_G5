// fichier : com/esprit/controllers/EventDetailsController.java
package com.esprit.controllers;

import com.esprit.Services.EvenementService;
import com.esprit.models.Evenement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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

    private Evenement evenement;
    private BorderPane mainContainer;
    private VBox sidebar;
    private final EvenementService service = new EvenementService();

    public void setEvent(Evenement evt, BorderPane container) {
        this.evenement = evt;
        this.mainContainer = container;

        titleLabel.setText(evt.getTitre());
        dateLabel.setText("📅 " + evt.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        adresseLabel.setText("📍 " + evt.getAdresse());
        descriptionLabel.setText("📝 " + evt.getDescription());

        // Events
        editButton.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutEvent.fxml"));
                Parent form = loader.load();
                AjoutEvent controller = loader.getController();
                controller.setMainContainer(mainContainer);
                controller.setMode("edit");
                controller.setEvenementToEdit(evt);
                controller.prefillFieldsIfEdit();
                mainContainer.setCenter(form);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        deleteButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cet événement ?", ButtonType.OK, ButtonType.CANCEL);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    new EvenementService().delete(evt.getId());
                    handleBack();
                }
            });
        });

        backButton.setOnAction(e -> handleBack());
    }

    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventsView.fxml"));
            Parent listView = loader.load();
            mainContainer.setCenter(listView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void handleDelete() {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cet événement ?", ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Confirmation");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                service.delete(evenement.getId());
                handleBack();
            }
        });
    }

    private void handleEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutEvent.fxml"));
            Parent form = loader.load();
            AjoutEvent controller = loader.getController();
            controller.setMode("edit");
            controller.setEvenementToEdit(evenement);
            controller.prefillFieldsIfEdit();
            mainContainer.setCenter(form);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
