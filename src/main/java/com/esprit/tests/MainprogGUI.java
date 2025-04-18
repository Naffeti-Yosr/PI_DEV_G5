package com.esprit.tests;

import com.esprit.controllers.AddUserController;
import com.esprit.controllers.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class MainprogGUI extends Application {

    private Stage primaryStage;
    private com.esprit.controllers.AdminDashboardController adminDashboardController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Energy Management System");
        showLoginScene();
    }

    public void showLoginScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login_page.fxml"));
            Parent root = loader.load();

            LoginController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(new Scene(root, 900, 600));
            primaryStage.show();
        } catch (IOException e) {
            showError("Could not load login screen", e);
        }
    }

    public void showRegisterScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddUser.fxml"));
            Parent root = loader.load();

            AddUserController controller = loader.getController();
            controller.setMainApp(this);

            primaryStage.setScene(new Scene(root, 900, 600));
            primaryStage.show();
        } catch (IOException e) {
            showError("Could not load registration screen", e);
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


    public void showAdminDashboardScene() {
        try {
            java.net.URL resource = getClass().getResource("/admin_dashboard.fxml");
            System.out.println("Loading admin_dashboard.fxml from: " + resource);
            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            // Set mainApp in controller
            adminDashboardController = loader.getController();
            adminDashboardController.setMainApp(this);

            primaryStage.setScene(new Scene(root, 900, 600));
            primaryStage.show();
        } catch (Exception e) {
            showError("Could not load admin dashboard screen", e);
        }
    }

    public com.esprit.controllers.AdminDashboardController getAdminDashboardController() {
        return adminDashboardController;
    }
}
