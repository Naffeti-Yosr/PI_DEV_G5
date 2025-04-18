package com.esprit.tests;

import com.esprit.controllers.AjoutEvent;
import com.esprit.models.Evenement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Stage primaryStage;
    private static App instance;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        instance = this;

        primaryStage.setTitle("Energy Management System");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/logoPi.jpeg")));

        showEventsScreen();
    }

    public static App getInstance() {
        return instance;
    }

    public void showEventsScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventsView.fxml"));
            Parent root = loader.load();
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

            primaryStage.setScene(new Scene(root, 1200, 800));
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
