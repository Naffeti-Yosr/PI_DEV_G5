package com.esprit.controllers;

import com.esprit.Services.EvenementService;
import com.esprit.models.Evenement;
import com.esprit.models.Poster;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventController {

    @FXML private VBox mainContainer;
    @FXML private VBox searchContainer;
    @FXML private TextField searchField;
    @FXML private FlowPane eventsFlowPane;

    private List<Evenement> allEvents;

    @FXML
    public void initialize() {
        EvenementService service = new EvenementService();
        allEvents = service.get();
        afficherEvenements(allEvents);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            List<Evenement> filtered = allEvents.stream()
                    .filter(e -> e.getTitre().toLowerCase().contains(newVal.toLowerCase()))
                    .toList();
            afficherEvenements(filtered);
        });
    }

    private void afficherEvenements(List<Evenement> events) {
        eventsFlowPane.getChildren().clear();
        for (Evenement evt : events) {
            VBox card = creerCarteEvenement(evt);
            eventsFlowPane.getChildren().add(card);
        }
    }

    private VBox creerCarteEvenement(Evenement evt) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setPrefWidth(220);

        // Image
        String path = "/default_event.png";
        if (evt.getPoster() != null && evt.getPoster().getImagePath() != null) {
            path = evt.getPoster().getImagePath();
        }

        ImageView imageView = new ImageView(new Image(getClass().getResource(path).toExternalForm()));
        imageView.setFitWidth(200);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        Label title = new Label(evt.getTitre());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label date = new Label(evt.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        date.setStyle("-fx-text-fill: #888;");

        Label badge = new Label();
        if (evt.getDate().toLocalDate().isEqual(LocalDate.now())) {
            badge.setText("🟢 Aujourd'hui");
            badge.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-padding: 2 6; -fx-background-radius: 5;");
        }

        card.getChildren().addAll(imageView, title, date);
        if (!badge.getText().isEmpty()) card.getChildren().add(badge);

        card.setOnMouseClicked(e -> openEventDetails(evt));
        return card;
    }

    private void openEventDetails(Evenement evt) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventDetails.fxml"));
            Parent detailsView = loader.load();

            EventDetailsController controller = loader.getController();
            controller.setEvent(evt, mainContainer, searchContainer);

            // Masquer la barre de recherche
            searchContainer.setVisible(false);
            searchContainer.setManaged(false);

            mainContainer.getChildren().set(1, detailsView); // Remplace la FlowPane
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}