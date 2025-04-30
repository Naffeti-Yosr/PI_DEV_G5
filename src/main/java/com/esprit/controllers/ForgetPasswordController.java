package com.esprit.controllers;

import com.esprit.models.User;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import com.esprit.utils.Validator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class ForgetPasswordController {

    private MainprogGUI mainApp;
    private final UserService userService = new UserService() {
        @Override
        public void add(User user) {

        }
    };

    // Form Elements
    @FXML private TextField tfEmail;
    @FXML private Label lblEmailError;
    @FXML private Button btnSubmit;
    @FXML private Button btnCancel;
    @FXML private ProgressIndicator progressIndicator;

    @FXML
    public void initialize() {
        setupEmailValidation();
        if (progressIndicator != null) {
            progressIndicator.setVisible(false);
        }
    }

    private void setupEmailValidation() {
        // Real-time email validation
        tfEmail.textProperty().addListener((obs, old, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!Validator.isValidEmail(newValue)) {
                    showFieldError("Please enter a valid email address");
                } else {
                    hideFieldError();
                }
            } else {
                hideFieldError();
            }
        });

        // Enable/disable submit button based on email validity
        btnSubmit.disableProperty().bind(
            tfEmail.textProperty().isEmpty()
            .or(tfEmail.textProperty().isNull())
        );
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        String email = tfEmail.getText().trim();
        
        if (!validateEmail(email)) {
            return;
        }

        showProgress(true);
        
        try {
            User user = userService.getUserByEmail(email);
            if (user == null) {
                showErrorAlert("Email Not Found", 
                    "No account was found with this email address.");
                return;
            }

            // Here you would typically:
            // 1. Generate a password reset token
            // 2. Save it to the database with an expiration
            // 3. Send an email with the reset link
            // For now, we'll just show a success message
            
            // Instead of showing success alert and returning to login,
            // navigate to UpdatePassword scene and pass the email

            try {
                if (mainApp != null) {
                    mainApp.showUpdatePasswordScene(email);
                } else {
                    showErrorAlert("Error", "Unable to navigate to update password screen.");
                }
            } catch (Exception ex) {
                showErrorAlert("Error", "Failed to open update password screen.");
            }

        } catch (Exception e) {
            showErrorAlert("Error", 
                "An error occurred while processing your request. Please try again.");
        } finally {
            showProgress(false);
        }
    }

    private boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            showFieldError("Please enter your email address");
            return false;
        }

        if (!Validator.isValidEmail(email)) {
            showFieldError("Please enter a valid email address");
            return false;
        }

        return true;
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        if (hasUnsavedChanges()) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Cancel");
            confirm.setHeaderText("Cancel Password Reset");
            confirm.setContentText("Are you sure you want to cancel the password reset process?");

            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    navigateToLogin();
                }
            });
        } else {
            navigateToLogin();
        }
    }

    private boolean hasUnsavedChanges() {
        return tfEmail != null && !tfEmail.getText().trim().isEmpty();
    }

    private void navigateToLogin() {
        if (mainApp != null) {
            mainApp.showLoginScene();
        } else {
            showErrorAlert("Error", "Unable to return to login screen");
        }
    }

    private void showFieldError(String message) {
        if (lblEmailError != null) {
            lblEmailError.setText(message);
            lblEmailError.setVisible(true);
        }
    }

    private void hideFieldError() {
        if (lblEmailError != null) {
            lblEmailError.setVisible(false);
        }
    }

private void showProgress(boolean show) {
    if (progressIndicator != null) {
        progressIndicator.setVisible(show);
    }
    // Remove direct setDisable calls on bound properties to avoid exception
    if (btnSubmit != null) {
        btnSubmit.disableProperty().unbind();
        btnSubmit.setDisable(show);
        if (!show) {
            btnSubmit.disableProperty().bind(
                tfEmail.textProperty().isEmpty()
                .or(tfEmail.textProperty().isNull())
            );
        }
    }
    if (tfEmail != null) {
        tfEmail.setDisable(show);
    }
}

    private void clearForm() {
        tfEmail.clear();
        hideFieldError();
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

    public void setMainApp(MainprogGUI mainApp) {
        this.mainApp = mainApp;
    }
}
