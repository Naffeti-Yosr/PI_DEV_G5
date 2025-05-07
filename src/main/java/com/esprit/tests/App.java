package com.esprit.tests;

import com.esprit.controllers.AjoutEvent;
import com.esprit.controllers.EventController;
import com.esprit.models.Evenement;
import com.esprit.models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage primaryStage;
    private static App instance;
    private User currentUser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        instance = this;

        // 👤 Simulation d’un utilisateur connecté (à remplacer par une vraie authentification plus tard)
        User fakeUser = new User();
        fakeUser.setId(1);
        fakeUser.setNom("Yosr");
        fakeUser.setPrenom("NFT");
        fakeUser.setEmail("yosr@example.com");
        fakeUser.setRole("ROLE_PARTICIPANT");
        setCurrentUser(fakeUser);

        primaryStage.setTitle("Energy Management System");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logoPi.jpeg")));

        showEventsScreen();
    }

    public static App getInstance() {
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void showEventsScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventsView.fxml"));
            Parent root = loader.load();

            EventController controller = loader.getController();
            controller.setCurrentUser(currentUser); // ✅ passage de l’utilisateur au contrôleur

            Scene scene = new Scene(root, 1200, 800);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Liste des Événements");
            primaryStage.show();
        } catch (Exception e) {
            showError("Erreur de chargement de la vue des événements", e);
        }
    }

    public void showAjoutEvent(Evenement evenementToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutEvent.fxml"));
            Parent root = loader.load();

            AjoutEvent controller = loader.getController();
            controller.setMode("edit");
            controller.setEvenementToEdit(evenementToEdit);
            controller.prefillFieldsIfEdit();
            controller.setMainContainer((BorderPane) root); // utile si tu veux faire retour

            Scene scene = new Scene(root, 1200, 800);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Modifier Événement");
            primaryStage.show();
        } catch (IOException e) {
            showError("Erreur lors de l'ouverture de l'écran d'édition", e);
        }
    }

    private void showError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de l'application");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
