package com.esprit.tests;

import com.esprit.controllers.AjoutEvent;
import com.esprit.models.Evenement;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class App extends Application {

    private Stage primaryStage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Energy Management System");
        // showAjoutEvent();
        // Appeler la vue des événements
        showEventsScreen();
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/logoPI.jpeg")));
    }

    public void showAjoutEvent(Evenement evenementToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutEvent.fxml"));
            Parent root = loader.load();

            AjoutEvent controller = loader.getController();
            controller.setMainApp(this);
            controller.setMode("edit");
            controller.setEvenementToEdit(evenementToEdit);
            controller.prefillFieldsIfEdit();

            primaryStage.setScene(new Scene(root, 900, 600));
            primaryStage.setTitle("Modifier Événement");
            primaryStage.show();

        } catch (IOException e) {
            showError("Erreur lors de l'ouverture de l'écran d'édition", e);
        }
    }

    public void showEventsScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventsView.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 900, 600));
            primaryStage.setTitle("Liste des Événements");
            primaryStage.show();
        } catch (IOException e) {
            showError("Erreur de chargement des événements", e);
        }
    }


    private void showError(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Application Error");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}