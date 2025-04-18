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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventController {

    @FXML private BorderPane mainContainer;
    @FXML private VBox searchContainer;
    @FXML private FlowPane eventsFlowPane;
    @FXML private TextField searchField;
    @FXML private HBox dayFilterBar;
    @FXML private ImageView logoImage;
    @FXML private VBox sidebar;
    @FXML private Button burgerButton;
    @FXML private VBox dayFilterContainer;
    @FXML private Button ajoutEventButton;

    private List<Evenement> allEvents;
    private String activeDay = null;

    @FXML
    public void initialize() {
        logoImage.setImage(new Image(getClass().getResource("/logoPi.jpeg").toExternalForm()));
        EvenementService service = new EvenementService();
        allEvents = service.get()
                .stream()
                .sorted((e1, e2) -> e1.getDate().compareTo(e2.getDate()))
                .toList();

        setupDayFilterBar();
        afficherEvenements(allEvents);

        // ✅ Bouton Ajouter Événement
        ajoutEventButton.setOnAction(e -> openAjoutEventForm());

        // ✅ Gestion du burger menu
        burgerButton.setOnAction(e -> toggleSidebar());

        // ✅ Clic extérieur pour fermer la sidebar
        mainContainer.addEventFilter(javafx.scene.input.MouseEvent.MOUSE_PRESSED, event -> {
            if (sidebar.isVisible() &&
                    !sidebar.localToScene(sidebar.getBoundsInLocal()).contains(event.getSceneX(), event.getSceneY())) {
                sidebar.setVisible(false);
                sidebar.setManaged(false);
            }
        });

        // ✅ Barre de recherche
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            List<Evenement> filtered = allEvents.stream()
                    .filter(e -> e.getTitre().toLowerCase().contains(newVal.toLowerCase()))
                    .toList();
            afficherEvenements(filtered);
        });
    }

    // ✅ Bien placée hors de initialize()
    private void openAjoutEventForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutEvent.fxml"));
            Parent ajoutForm = loader.load();

            AjoutEvent controller = loader.getController();
            controller.setMode("add"); // mode ajout
            controller.setMainContainer(mainContainer);

            searchContainer.setVisible(false);
            searchContainer.setManaged(false);
            dayFilterContainer.setVisible(false);
            dayFilterContainer.setManaged(false);

            mainContainer.setCenter(ajoutForm);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void toggleSidebar() {
        boolean isVisible = sidebar.isVisible();
        sidebar.setVisible(!isVisible);
        sidebar.setManaged(!isVisible);
    }

    private void setupDayFilterBar() {
        String[] jours = {"All", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        for (String jour : jours) {
            Button btn = new Button(jour);
            btn.getStyleClass().add("day-button");
            btn.setOnAction(e -> toggleDayFilter(jour, btn));
            dayFilterBar.getChildren().add(btn);
        }
    }

    private void toggleDayFilter(String jour, Button btn) {
        if (jour.equals("All")) {
            activeDay = null;
            dayFilterBar.getChildren().forEach(n -> n.getStyleClass().remove("active"));
            afficherEvenements(allEvents);
            return;
        }

        dayFilterBar.getChildren().forEach(n -> n.getStyleClass().remove("active"));
        activeDay = jour;
        btn.getStyleClass().add("active");

        int jourIndex = switch (jour) {
            case "Lundi" -> 1;
            case "Mardi" -> 2;
            case "Mercredi" -> 3;
            case "Jeudi" -> 4;
            case "Vendredi" -> 5;
            case "Samedi" -> 6;
            case "Dimanche" -> 7;
            default -> -1;
        };

        List<Evenement> filtered = allEvents.stream()
                .filter(evt -> evt.getDate().getDayOfWeek().getValue() == jourIndex)
                .toList();

        afficherEvenements(filtered);
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

        String path = "/default_event.png";
        if (evt.getPoster() != null && evt.getPoster().getImagePath() != null) {
            path = evt.getPoster().getImagePath();
        }

        ImageView imageView = new ImageView(new Image(getClass().getResource(path).toExternalForm()));
        imageView.setFitWidth(200);
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        Label title = new Label(evt.getTitre());
        title.getStyleClass().add("title");

        Label date = new Label(evt.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        date.getStyleClass().add("date");

        Label badge = new Label();
        LocalDate eventDay = evt.getDate().toLocalDate();
        LocalDate today = LocalDate.now();

        if (eventDay.equals(today)) {
            badge.setText("🟢 Aujourd'hui");
            badge.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-padding: 2 6; -fx-background-radius: 5;");
        } else if (eventDay.equals(today.plusDays(1))) {
            badge.setText("🔵 Demain");
            badge.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 2 6; -fx-background-radius: 5;");
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
            controller.setEvent(evt, mainContainer, searchContainer, dayFilterContainer);

            searchContainer.setVisible(false);
            searchContainer.setManaged(false);
            dayFilterContainer.setVisible(false);
            dayFilterContainer.setManaged(false);

            mainContainer.setCenter(detailsView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restaurerVuePrincipale() {
        searchContainer.setVisible(true);
        searchContainer.setManaged(true);
        dayFilterContainer.setVisible(true);
        dayFilterContainer.setManaged(true);
    }

    public void setMainContainer(BorderPane mainContainer) {
        this.mainContainer = mainContainer;
    }
}
