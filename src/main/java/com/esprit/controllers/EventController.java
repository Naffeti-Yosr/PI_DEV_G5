package com.esprit.controllers;

import com.esprit.Services.EvenementService;
import com.esprit.models.Evenement;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventController {

    @FXML private BorderPane mainContainer;
    @FXML private FlowPane eventsFlowPane;
    @FXML private VBox sidebar;
    @FXML private HBox dayFilterBar;
    @FXML private TextField searchField;
    @FXML private ImageView logoImage;
    @FXML private Button menuButton;

    private List<Evenement> allEvents;
    private String activeDay = null;

    @FXML
    public void initialize() {
        // Logo en haut à gauche
        logoImage.setImage(new Image(getClass().getResource("/logoPI.jpeg").toExternalForm()));

        // Affichage ou masquage de la sidebar
        menuButton.setOnAction(e -> {
            boolean visible = !sidebar.isVisible();
            sidebar.setVisible(visible);
            sidebar.setManaged(visible);
        });

        // Charger les événements
        EvenementService service = new EvenementService();
        allEvents = service.get();

        // Création de la barre des jours
        String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        for (String jour : jours) {
            Button btn = new Button(jour);
            btn.getStyleClass().add("day-button");
            btn.setOnAction(e -> toggleDayFilter(jour, btn));
            dayFilterBar.getChildren().add(btn);
        }

        // Listener de recherche
        searchField.textProperty().addListener((obs, o, n) -> refreshEvents());

        // Affichage initial
        refreshEvents();
    }

    private void toggleDayFilter(String jour, Button btn) {
        if (jour.equals(activeDay)) {
            activeDay = null;
            btn.getStyleClass().remove("active");
        } else {
            dayFilterBar.getChildren().forEach(node -> node.getStyleClass().remove("active"));
            activeDay = jour;
            btn.getStyleClass().add("active");
        }
        refreshEvents();
    }

    private void refreshEvents() {
        eventsFlowPane.getChildren().clear();
        String searchText = searchField.getText().toLowerCase();

        for (Evenement evt : allEvents) {
            boolean matchJour = (activeDay == null) || evt.getDate().getDayOfWeek().toString().equalsIgnoreCase(activeDay);
            boolean matchTitre = evt.getTitre().toLowerCase().contains(searchText);

            if (matchJour && matchTitre) {
                eventsFlowPane.getChildren().add(createCard(evt));
            }
        }
    }

    private VBox createCard(Evenement evt) {
        VBox card = new VBox(6);
        card.getStyleClass().add("card");
        card.setPrefWidth(200);

        // Image de l'événement
        ImageView iv = new ImageView();
        String imagePath = (evt.getPoster() != null && evt.getPoster().getImagePath() != null)
                ? evt.getPoster().getImagePath()
                : "/default_event.png";

        try {
            iv.setImage(new Image(getClass().getResource(imagePath).toExternalForm()));
        } catch (Exception e) {
            iv.setImage(new Image(getClass().getResource("/default_event.png").toExternalForm()));
        }

        iv.setFitWidth(200);
        iv.setFitHeight(120);
        iv.setPreserveRatio(true);

        // Titre
        Label title = new Label(evt.getTitre());
        title.getStyleClass().add("title");

        // Date
        Label date = new Label("📅 " + evt.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        date.getStyleClass().add("date");

        // Heure
        Label time = new Label("⏰ " + evt.getDate().format(DateTimeFormatter.ofPattern("HH:mm")));
        time.getStyleClass().add("time");

        card.getChildren().addAll(iv, title, date, time);

        // Click → vers détails
        card.setOnMouseClicked(e -> openEventDetails(evt));

        return card;
    }

    private void openEventDetails(Evenement evt) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventDetails.fxml"));
            Parent detailView = loader.load();
            EventDetailsController controller = loader.getController();
            controller.setEvent(evt, mainContainer);
            mainContainer.setCenter(detailView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
