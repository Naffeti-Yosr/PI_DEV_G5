package com.esprit.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainProgGUI extends Application {

    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        showDashboard();
    }

    public static void showDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainProgGUI.class.getResource("/DashboardController.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 800); // Ajout des dimensions

        scene.getStylesheets().add(MainProgGUI.class.getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Smart Waste Management Dashboard");
        primaryStage.getIcons().add(new javafx.scene.image.Image(MainProgGUI.class.getResourceAsStream("/images/logo.jpg")));
        primaryStage.show();
    }

    public static void showBinForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainProgGUI.class.getResource("/Bin-form.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 800); // Ajout des dimensions

        scene.getStylesheets().add(MainProgGUI.class.getResource("/styles.css").toExternalForm());

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add/Edit Bin");
        stage.show();
    }

    public static void showTruckForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainProgGUI.class.getResource("/Truck-form.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 800); // Ajout des dimensions

        scene.getStylesheets().add(MainProgGUI.class.getResource("/styles.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add/Edit Truck");
        stage.show();
    }
}