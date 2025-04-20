package com.esprit.tests;

import com.esprit.controllers.*;
import com.esprit.models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class MainprogGUI extends Application {
    private <T> void loadScene(String fxmlPath, int width, int height, SceneControllerConsumer<T> controllerSetup) {
        try {
            java.net.URL fxmlUrl = getClass().getResource(fxmlPath);
            if (fxmlUrl == null) {
                // Try without leading slash
                fxmlUrl = getClass().getResource(fxmlPath.startsWith("/") ? fxmlPath.substring(1) : fxmlPath);
            }
            if (fxmlUrl == null) {
                throw new IOException("FXML resource not found: " + fxmlPath);
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();

            T controller = loader.getController();
            controllerSetup.accept(controller);

            primaryStage.setScene(new Scene(root, width, height));
            primaryStage.show();
        } catch (IOException e) {
            showError("Could not load " + fxmlPath, e);
        }
    }

    private Stage primaryStage;
    private AdminDashboardController adminDashboardController;

    private static final int WIDTH = 900;
    private static final int HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Solaris Desktop");
        showLoginScene();
    }

    public void showLoginScene() {
        loadScene("/login_page.fxml", WIDTH, HEIGHT, (LoginController controller) -> {
            controller.setMainApp(this);
        });
    }

    public void showRegisterScene() {
        loadScene("/Registration_Page.fxml", WIDTH, HEIGHT, (RegistrationController controller) -> {
            controller.setMainApp(this);
        });
    }

    public void showAdminDashboardScene() {
        loadScene("/admin_dashboard.fxml", WIDTH, HEIGHT, (AdminDashboardController controller) -> {
            adminDashboardController = controller;
            controller.setMainApp(this);
        });
    }

    public void showManageAddUserScene() {
        loadScene("/AdminAddUser.fxml", WIDTH, HEIGHT, (AdminAddUserController controller) -> {
            controller.setMainApp(this);
        });
    }

    public void showManageUserDashboardScene() {
        loadScene("/manage_user_dashboard.fxml", WIDTH, HEIGHT, (ManageUserDashboardController controller) -> {
            controller.setMainApp(this);
        });
    }

    public void showAdminModifyUserScene(User user) {
        loadScene("/AdminModifyUser.fxml", WIDTH, HEIGHT, (AdminModifyController controller) -> {
            controller.setMainApp(this);
            controller.setUser(user);
        });
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

    public AdminDashboardController getAdminDashboardController() {
        return adminDashboardController;
    }

    @FunctionalInterface
    interface SceneControllerConsumer<T> {
        void accept(T controller);
    }
}