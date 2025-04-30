package com.esprit.controllers;

import com.esprit.models.User;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import com.esprit.utils.Validator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class UpdatePasswordController {

    private MainprogGUI mainApp;
    private final UserService userService = new UserService() {
        @Override
        public void add(User user) {
            // No implementation needed here
        }
    };

    private String userEmail;

    @FXML private PasswordField pfNewPassword;
    @FXML private PasswordField pfConfirmPassword;
    @FXML private Button btnSubmit;
    @FXML private Button btnCancel;
    @FXML private Label lblPasswordError;

    @FXML
    public void initialize() {
        setupPasswordValidation();
    }

    private void setupPasswordValidation() {
        pfNewPassword.textProperty().addListener((obs, old, newValue) -> {
            validatePasswords();
        });
        pfConfirmPassword.textProperty().addListener((obs, old, newValue) -> {
            validatePasswords();
        });
        btnSubmit.setDisable(true);
    }

    private void validatePasswords() {
        String newPassword = pfNewPassword.getText();
        String confirmPassword = pfConfirmPassword.getText();

        if (newPassword == null || newPassword.isEmpty() || !Validator.isValidPassword(newPassword)) {
            showFieldError("Password must be at least 8 characters with uppercase, lowercase, and digits");
            btnSubmit.setDisable(true);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showFieldError("Passwords do not match");
            btnSubmit.setDisable(true);
            return;
        }

        hideFieldError();
        btnSubmit.setDisable(false);
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        String newPassword = pfNewPassword.getText();

        if (userEmail == null || userEmail.isEmpty()) {
            showErrorAlert("Error", "User email is missing. Cannot update password.");
            return;
        }

        if (!Validator.isValidPassword(newPassword)) {
            showFieldError("Password must be at least 8 characters with uppercase, lowercase, and digits");
            return;
        }

        try {
            User user = userService.getUserByEmail(userEmail);
            if (user == null) {
                showErrorAlert("Error", "No user found with the provided email.");
                return;
            }

            user.setPassword(newPassword);
            userService.update(user);

            showSuccessAlert("Success", "Password updated successfully.");
            navigateToLogin();

        } catch (Exception e) {
            showErrorAlert("Error", "Failed to update password. Please try again.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        navigateToLogin();
    }

    private void navigateToLogin() {
        if (mainApp != null) {
            mainApp.showLoginScene();
        } else {
            showErrorAlert("Error", "Unable to navigate to login screen.");
        }
    }

    private void showFieldError(String message) {
        if (lblPasswordError != null) {
            lblPasswordError.setText(message);
            lblPasswordError.setVisible(true);
        }
    }

    private void hideFieldError() {
        if (lblPasswordError != null) {
            lblPasswordError.setVisible(false);
        }
    }

    private void showSuccessAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    public void setMainApp(MainprogGUI mainApp) {
        this.mainApp = mainApp;
    }

    public void handleBackToLogin(ActionEvent actionEvent) {

    }
}
