package com.esprit.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseController<T> implements Initializable {

    @Override
    public abstract void initialize(URL location, ResourceBundle resources);

    protected void switchScene(Stage stage, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        stage.getScene().setRoot(root);
    }

    /**
     * Loads an FXML view, switches the scene root to the loaded view, and returns the controller instance.
     * @param stage The stage whose scene root will be switched.
     * @param fxmlPath The path to the FXML file.
     * @param <C> The type of the controller.
     * @return The controller instance of the loaded FXML.
     * @throws IOException If loading the FXML fails.
     */
    protected <C> C loadViewAndSwitchScene(Stage stage, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        stage.getScene().setRoot(root);
        return loader.getController();
    }


}
