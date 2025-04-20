package com.esprit.controllers;

import com.esprit.tests.MainprogGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;

public class AdminDashboardController {

    private MainprogGUI mainApp;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnManageUsers;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnLogout;

    @FXML
    private Label lblUsername;

    public void setMainApp(MainprogGUI mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        btnDashboard.setTooltip(new javafx.scene.control.Tooltip("View dashboard overview"));
        btnManageUsers.setTooltip(new javafx.scene.control.Tooltip("Add, edit, or remove users"));
        btnReports.setTooltip(new javafx.scene.control.Tooltip("View system reports"));
        btnSettings.setTooltip(new javafx.scene.control.Tooltip("Configure system settings"));
        btnLogout.setTooltip(new javafx.scene.control.Tooltip("Logout from the system"));

        btnManageUsers.setOnAction(event -> {
            if (mainApp != null) {
                mainApp.showManageUserDashboardScene();
            }
        });
    }

    @FXML
    void handleLogout(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showLoginScene();
        }
    }

    public void setUsername(String email) {
        if (lblUsername != null) {
            lblUsername.setText(email);
        }
    }

    public void addUser(ActionEvent actionEvent) {
    }
}
