package com.esprit.controllers;

import com.esprit.models.User;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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

    private UserService userService = new UserService();

    public void setMainApp(MainprogGUI mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        btnDashboard.setTooltip(new Tooltip("View dashboard overview"));
        btnManageUsers.setTooltip(new Tooltip("Add, edit, or remove users"));
        btnReports.setTooltip(new Tooltip("View system reports"));
        btnSettings.setTooltip(new Tooltip("Configure system settings"));
        btnLogout.setTooltip(new Tooltip("Logout from the system"));
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showLoginScene();
        }
    }

    public void setUsername(String email) {
        User user = userService.getUserByEmail(email);
        String displayName = (user != null) ? user.getNom() + " " + user.getPrenom() : email;
        if (lblUsername != null) {
            lblUsername.setText(displayName);
        }
    }
}
