package com.esprit.tests;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainProgGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BlogDashboardView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 800);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Blog Dashboard");
        primaryStage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/images/logo.jfif")));
        primaryStage.show();
    }
}