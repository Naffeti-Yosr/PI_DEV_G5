package com.esprit.tests;

import com.esprit.controllers.*;
import com.esprit.models.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.Modality;

import java.io.IOException;

public class MainprogGUI extends Application {
    private Stage primaryStage;
    private AdminDashboardController adminDashboardController;
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Solaris Desktop");

        try {
            System.out.println("Initializing database and test data...");
            System.out.println("Database initialization complete.");
        } catch (Exception e) {
            System.err.println("Error initializing database:");
            e.printStackTrace();
            showError("Database Error", e);
        }

        showLoginScene();
    }

    private <T> void loadModal(String fxmlPath, String title, Class<T> controllerClass, ControllerInitializer<T> initializer) {
        try {
            System.out.println("Loading FXML from path: " + fxmlPath);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainprogGUI.class.getResource(fxmlPath));
            
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find FXML file: " + fxmlPath);
            }
            
            System.out.println("FXML URL: " + loader.getLocation());
            
            Parent root = loader.load();
            System.out.println("FXML loaded successfully");

            Stage modalStage = new Stage();
            modalStage.setTitle(title);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(primaryStage);

            T controller = controllerClass.cast(loader.getController());
            System.out.println("Controller loaded: " + controller.getClass().getSimpleName());
            initializer.initialize(controller);

            Scene scene = new Scene(root);
            
            // Print available stylesheets
            System.out.println("Available stylesheets:");
            for (String stylesheet : scene.getStylesheets()) {
                System.out.println("- " + stylesheet);
            }
            
            // Add stylesheets programmatically as a backup
            String[] stylesheets = {
                "/styles/common.css",
                "/styles/admin_dashboard_fixed.css",
                "/styles/dashboard_cards.css"
            };
            
            for (String stylesheet : stylesheets) {
                String cssUrl = MainprogGUI.class.getResource(stylesheet).toExternalForm();
                if (!scene.getStylesheets().contains(cssUrl)) {
                    System.out.println("Adding stylesheet: " + cssUrl);
                    scene.getStylesheets().add(cssUrl);
                }
            }

            modalStage.setScene(scene);
            modalStage.showAndWait();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + fxmlPath);
            e.printStackTrace();
            showError("Could not load " + fxmlPath, e);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            showError("Error loading scene", e);
        }
    }

    public <T> void loadScene(String fxmlPath, Class<T> controllerClass, ControllerInitializer<T> initializer) {
        try {
            System.out.println("Loading FXML from path: " + fxmlPath);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainprogGUI.class.getResource(fxmlPath));
            
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find FXML file: " + fxmlPath);
            }
            
            // Print the actual URL being used
            System.out.println("FXML URL: " + loader.getLocation());
            
            Parent root = loader.load();
            System.out.println("FXML loaded successfully");

            T controller = controllerClass.cast(loader.getController());
            System.out.println("Controller loaded: " + controller.getClass().getSimpleName());
            initializer.initialize(controller);

            Scene scene = new Scene(root, WIDTH, HEIGHT);
            
            // Print available stylesheets
            System.out.println("Available stylesheets:");
            for (String stylesheet : scene.getStylesheets()) {
                System.out.println("- " + stylesheet);
            }
            
            // Add stylesheets programmatically as a backup
            String[] stylesheets = {
                "/styles/common.css",
                "/styles/admin_dashboard_fixed.css",
                "/styles/dashboard_cards.css"
            };
            
            for (String stylesheet : stylesheets) {
                String cssUrl = MainprogGUI.class.getResource(stylesheet).toExternalForm();
                if (!scene.getStylesheets().contains(cssUrl)) {
                    System.out.println("Adding stylesheet: " + cssUrl);
                    scene.getStylesheets().add(cssUrl);
                }
            }

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + fxmlPath);
            e.printStackTrace();
            showError("Could not load " + fxmlPath, e);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            showError("Error loading scene", e);
        }
    }

    public void showLoginScene() {
        loadScene("/login_page.fxml", LoginController.class, 
            controller -> controller.setMainApp(this));
    }

    public void showRegisterScene() {
        loadScene("/Registration_Page.fxml", RegistrationController.class,
            controller -> controller.setMainApp(this));
    }

    public void showAdminDashboardScene() {
        loadScene("/admin_dashboard.fxml", AdminDashboardController.class,
            controller -> {
                adminDashboardController = controller;
                controller.setMainApp(this);
                controller.setUsername(System.getProperty("currentUser"));
                controller.refreshDashboard();
            });
    }

    public void showManageUserDashboardScene() {
        loadScene("/manage_user_dashboard.fxml", ManageUserDashboardController.class,
            controller -> controller.setMainApp(this));
    }

    public void showAdminModifyUserScene(User user) {
        System.out.println("Showing Modify User Scene for user: " + user.getNom());
        loadModal("/AdminModifyUser.fxml", "Modify User", AdminModifyController.class,
            controller -> {
                controller.setMainApp(this);
                controller.setUser(user);
            });
    }


    public void showAdminAddUserScene() {
        System.out.println("Loading Add User Scene in primary stage");
        loadScene("/AdminAddUser.fxml", AdminAddUserController.class,
            controller -> controller.setMainApp(this));
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
    public void showForgetPasswordScene() {
        loadScene("/ForgetPassword.fxml", ForgetPasswordController.class,
                controller -> controller.setMainApp(this));
    }
    public void showReportsScene() {
        loadScene("/Reports.fxml", ReportsController.class,
                controller -> controller.setMainApp(this));
    }

    public void showClaimsScene() {
        loadScene("/Claims.fxml", ClaimsController.class,
                controller -> controller.setMainApp(this));
    }

    public void showUpdatePasswordScene(String email) {
        loadScene("/UpdatePassword.fxml", com.esprit.controllers.UpdatePasswordController.class,
            controller -> {
                controller.setMainApp(this);
                controller.setUserEmail(email);
            });
    }

    public void loadSceneInPrimaryStage(String fxmlPath, String title) {
        try {
            System.out.println("Loading FXML from path: " + fxmlPath);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainprogGUI.class.getResource(fxmlPath));
            
            if (loader.getLocation() == null) {
                throw new IOException("Cannot find FXML file: " + fxmlPath);
            }
            
            System.out.println("FXML URL: " + loader.getLocation());
            
            Parent root = loader.load();
            System.out.println("FXML loaded successfully");

            // Set the MainApp reference in the controller if it has setMainApp method
            Object controller = loader.getController();
            if (controller != null) {
                try {
                    java.lang.reflect.Method setMainApp = controller.getClass().getMethod("setMainApp", MainprogGUI.class);
                    setMainApp.invoke(controller, this);
                } catch (NoSuchMethodException e) {
                    // Controller doesn't have setMainApp method, that's okay
                    System.out.println("Note: Controller doesn't have setMainApp method");
                } catch (Exception e) {
                    System.err.println("Error setting MainApp in controller: " + e.getMessage());
                }
            }

            Scene scene = new Scene(root, WIDTH, HEIGHT);
            
            // Add stylesheets
            String[] stylesheets = {
                "/styles/common.css",
                "/styles/admin_dashboard_fixed.css",
                "/styles/dashboard_cards.css"
            };
            
            for (String stylesheet : stylesheets) {
                String cssUrl = MainprogGUI.class.getResource(stylesheet).toExternalForm();
                if (!scene.getStylesheets().contains(cssUrl)) {
                    scene.getStylesheets().add(cssUrl);
                }
            }

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + fxmlPath);
            e.printStackTrace();
            showError("Could not load " + fxmlPath, e);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            showError("Error loading scene", e);
        }
    }


    @FunctionalInterface
    public interface ControllerInitializer<T> {
        void initialize(T controller);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
