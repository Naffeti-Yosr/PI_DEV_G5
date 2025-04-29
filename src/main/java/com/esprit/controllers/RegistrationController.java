package com.esprit.controllers;

import com.esprit.models.User;
import com.esprit.models.UserStatus;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import com.esprit.utils.Validator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.ZoneId;

public class RegistrationController {

    private MainprogGUI mainApp;
    private final UserService userService = new UserService() {
        @Override
        public void add(User user) {

        }
    };

    // Form Elements
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private DatePicker dpBirthDate;
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfPassword;
    @FXML private PasswordField tfConfirmPassword;
    @FXML private TextField tfAddress;
    @FXML private TextField tfPhoneNumber;

    // Error Labels
    @FXML private Label lblNomError;
    @FXML private Label lblPrenomError;
    @FXML private Label lblEmailError;
    @FXML private Label lblPasswordError;
    @FXML private Label lblConfirmPasswordError;
    @FXML private Label lblBirthDateError;
    @FXML private Label lblAddressError;
    @FXML private Label lblPhoneError;

    @FXML
    public void initialize() {
        setupFormValidation();
        clearErrorLabels();
    }

    private void setupFormValidation() {
        // Real-time validation
        tfEmail.textProperty().addListener((obs, old, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!Validator.isValidEmail(newValue)) {
                    showFieldError(lblEmailError, "Please enter a valid email address");
                } else if (userService.getUserByEmail(newValue) != null) {
                    showFieldError(lblEmailError, "This email is already in use");
                } else {
                    hideFieldError(lblEmailError);
                }
            } else {
                hideFieldError(lblEmailError);
            }
        });

        tfPassword.textProperty().addListener((obs, old, newValue) -> {
            if (!newValue.isEmpty() && !Validator.isValidPassword(newValue)) {
                showFieldError(lblPasswordError, "Password must be at least 8 characters with uppercase, lowercase, and digits");
            } else {
                hideFieldError(lblPasswordError);
                validatePasswordMatch();
            }
        });

        tfConfirmPassword.textProperty().addListener((obs, old, newValue) -> {
            validatePasswordMatch();
        });

        dpBirthDate.valueProperty().addListener((obs, old, newValue) -> {
            if (newValue != null && !Validator.isValidBirthDate(newValue)) {
                showFieldError(lblBirthDateError, "Birth date cannot be in the future and user must be at least 18");
            } else {
                hideFieldError(lblBirthDateError);
            }
        });

        tfPhoneNumber.textProperty().addListener((obs, old, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d{8,}")) {
                showFieldError(lblPhoneError, "Phone number must contain at least 8 digits");
            } else {
                hideFieldError(lblPhoneError);
            }
        });
    }

    private void validatePasswordMatch() {
        if (!tfPassword.getText().isEmpty() && !tfConfirmPassword.getText().isEmpty()) {
            if (!tfPassword.getText().equals(tfConfirmPassword.getText())) {
                showFieldError(lblConfirmPasswordError, "Passwords do not match");
            } else {
                hideFieldError(lblConfirmPasswordError);
            }
        }
    }

    @FXML
    void addPerson(ActionEvent event) {
        System.out.println("addPerson called");
        clearErrorLabels();
        
        if (!validateForm()) {
            System.out.println("Form validation failed");
            return;
        }

        try {
            User user = createUserFromForm();
            userService.addUserForRegistration(user);
            
            System.out.println("User registration successful");
            showSuccessAlert("Success", "Registration successful! Please wait for admin approval.");
            clearFields();
            handleBackToLogin(null);

        } catch (Exception e) {
            System.out.println("Exception in addPerson: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Error", "Failed to register user: " + e.getMessage());
        }
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (tfNom.getText().isEmpty()) {
            System.out.println("Validation failed: Name is required");
            showFieldError(lblNomError, "Name is required");
            isValid = false;
        }

        if (tfPrenom.getText().isEmpty()) {
            System.out.println("Validation failed: First name is required");
            showFieldError(lblPrenomError, "First name is required");
            isValid = false;
        }

        if (tfEmail.getText().isEmpty() || !Validator.isValidEmail(tfEmail.getText())) {
            System.out.println("Validation failed: Valid email is required");
            showFieldError(lblEmailError, "Valid email is required");
            isValid = false;
        } else if (userService.getUserByEmail(tfEmail.getText()) != null) {
            System.out.println("Validation failed: Email already in use");
            showFieldError(lblEmailError, "This email is already in use");
            isValid = false;
        }

        if (tfPassword.getText().isEmpty() || !Validator.isValidPassword(tfPassword.getText())) {
            System.out.println("Validation failed: Valid password is required");
            showFieldError(lblPasswordError, "Valid password is required");
            isValid = false;
        }

        if (!tfPassword.getText().equals(tfConfirmPassword.getText())) {
            System.out.println("Validation failed: Passwords do not match");
            showFieldError(lblConfirmPasswordError, "Passwords do not match");
            isValid = false;
        }

        if (dpBirthDate.getValue() == null || !Validator.isValidBirthDate(dpBirthDate.getValue())) {
            System.out.println("Validation failed: Valid birth date is required");
            showFieldError(lblBirthDateError, "Valid birth date is required");
            isValid = false;
        }

        if (tfAddress.getText().isEmpty()) {
            System.out.println("Validation failed: Address is required");
            showFieldError(lblAddressError, "Address is required");
            isValid = false;
        }

        if (tfPhoneNumber.getText().isEmpty() || !tfPhoneNumber.getText().matches("\\d{8,}")) {
            System.out.println("Validation failed: Valid phone number is required");
            showFieldError(lblPhoneError, "Valid phone number is required (at least 8 digits)");
            isValid = false;
        }

        return isValid;
    }

    private User createUserFromForm() {
        User user = new User(
            tfNom.getText().trim(),
            tfPrenom.getText().trim(),
            java.util.Date.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()),
            tfEmail.getText().trim(),
            tfAddress.getText().trim(),
            tfPhoneNumber.getText().trim(),
            tfPassword.getText()
        );
        // Status will be set to NON_CONFIRMED by default in User constructor
        return user;
    }

    @FXML
    void handleBackToLogin(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showLoginScene();
        } else {
            showErrorAlert("Error", "Main application reference is missing!");
        }
    }

    private void showFieldError(Label errorLabel, String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        }
    }

    private void hideFieldError(Label errorLabel) {
        if (errorLabel != null) {
            errorLabel.setVisible(false);
        }
    }

    private void clearErrorLabels() {
        Label[] errorLabels = {lblNomError, lblPrenomError, lblEmailError, 
                             lblPasswordError, lblConfirmPasswordError, 
                             lblBirthDateError, lblAddressError, lblPhoneError};
        for (Label label : errorLabels) {
            if (label != null) {
                label.setVisible(false);
            }
        }
    }

    private void clearFields() {
        tfNom.clear();
        tfPrenom.clear();
        dpBirthDate.setValue(null);
        tfEmail.clear();
        tfPassword.clear();
        tfConfirmPassword.clear();
        tfAddress.clear();
        tfPhoneNumber.clear();
        clearErrorLabels();
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
