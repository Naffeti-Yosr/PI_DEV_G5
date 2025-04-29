package com.esprit.controllers;

import com.esprit.models.User;
import com.esprit.models.UserRole;
import com.esprit.models.UserStatus;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import com.esprit.utils.Validator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
//helllo0017

public class AdminAddUserController {

    private MainprogGUI mainApp;
    private final UserService userService;

    // Navigation Elements
    @FXML private Button btnDashboard;
    @FXML private Button btnManageUsers;
    @FXML private Button btnProducts;
    @FXML private Button btnEvents;
    @FXML private Button btnBlog;
    @FXML private Button btnReports;
    @FXML private Button btnSettings;
    @FXML private Button btnLogout;
    @FXML private Label lblUsername;

    // Form Elements
    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfPassword;
    @FXML private ComboBox<UserRole> cbRole;
    @FXML private ComboBox<UserStatus> cbStatus;
    @FXML private DatePicker dpBirthDate;
    @FXML private TextField tfAddress;
    @FXML private TextField tfPhoneNumber;

    // Error Labels
    @FXML private Label lblNomError;
    @FXML private Label lblPrenomError;
    @FXML private Label lblEmailError;
    @FXML private Label lblPasswordError;
    @FXML private Label lblRoleError;
    @FXML private Label lblStatusError;
    @FXML private Label lblBirthDateError;
    @FXML private Label lblAddressError;
    @FXML private Label lblPhoneError;

    // Action Buttons
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    public AdminAddUserController() {
        this.userService = new UserService() {
            @Override
            public void add(User user) {

            }
        };
    }

    @FXML
    public void initialize() {
        setupNavigation();
        setupFormValidation();
        setupRoleComboBox();
        setupStatusComboBox();
        setupButtons();
        clearErrorLabels();
        styleActiveButton(btnManageUsers);
    }

    private void setupButtons() {
        if (btnSave != null) {
            btnSave.setOnAction(this::handleSave);
        }
        if (btnCancel != null) {
            btnCancel.setOnAction(this::handleCancel);
        }
    }

    private void setupNavigation() {
        // Setup tooltips
        btnDashboard.setTooltip(new Tooltip("View dashboard overview"));
        btnManageUsers.setTooltip(new Tooltip("Manage users"));
        btnProducts.setTooltip(new Tooltip("Manage products"));
        btnEvents.setTooltip(new Tooltip("Manage events"));
        btnBlog.setTooltip(new Tooltip("Manage blog posts"));
        btnReports.setTooltip(new Tooltip("View reports"));
        btnSettings.setTooltip(new Tooltip("Configure settings"));
        btnLogout.setTooltip(new Tooltip("Logout from system"));
    }

    private void setupFormValidation() {
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
            }
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

    private void setupRoleComboBox() {
        cbRole.setItems(FXCollections.observableArrayList(UserRole.values()));
    }

    private void setupStatusComboBox() {
        cbStatus.setItems(FXCollections.observableArrayList(UserStatus.values()));
    }

    private void styleActiveButton(Button activeButton) {
        // Remove selected style from all buttons
        btnDashboard.getStyleClass().remove("selected");
        btnManageUsers.getStyleClass().remove("selected");
        btnProducts.getStyleClass().remove("selected");
        btnEvents.getStyleClass().remove("selected");
        btnBlog.getStyleClass().remove("selected");
        btnReports.getStyleClass().remove("selected");
        btnSettings.getStyleClass().remove("selected");

        // Add selected style to active button
        activeButton.getStyleClass().add("selected");
    }

    @FXML
    private void handleSave(ActionEvent event) {
        clearErrorLabels();

        if (!validateForm()) {
            return;
        }

        try {
            User newUser = createUserFromForm();
            userService.addUserByAdmin(newUser);
            showSuccessAlert("Success", "User added successfully");
            handleManageUsers();
        } catch (Exception e) {
            showErrorAlert("Error", "Could not add user: " + e.getMessage());
        }
    }

    private User createUserFromForm() {
        User user = new User();
        user.setNom(tfNom.getText().trim());
        user.setPrenom(tfPrenom.getText().trim());
        user.setEmail(tfEmail.getText().trim());
        user.setPassword(tfPassword.getText());
        user.setRole(cbRole.getValue());
        user.setStatus(cbStatus.getValue());
        user.setBirth_date(Date.valueOf(dpBirthDate.getValue()));
        user.setAddress(tfAddress.getText().trim());
        user.setPhoneNumber(tfPhoneNumber.getText().trim());
        return user;
    }

    private boolean validateForm() {
        boolean isValid = true;

        if (tfNom.getText().isEmpty()) {
            showFieldError(lblNomError, "Name is required");
            isValid = false;
        }

        if (tfPrenom.getText().isEmpty()) {
            showFieldError(lblPrenomError, "First name is required");
            isValid = false;
        }

        if (tfEmail.getText().isEmpty() || !Validator.isValidEmail(tfEmail.getText())) {
            showFieldError(lblEmailError, "Valid email is required");
            isValid = false;
        } else if (userService.getUserByEmail(tfEmail.getText()) != null) {
            showFieldError(lblEmailError, "This email is already in use");
            isValid = false;
        }

        if (tfPassword.getText().isEmpty() || !Validator.isValidPassword(tfPassword.getText())) {
            showFieldError(lblPasswordError, "Valid password is required");
            isValid = false;
        }

        if (cbRole.getValue() == null) {
            showFieldError(lblRoleError, "Role is required");
            isValid = false;
        }

        if (cbStatus.getValue() == null) {
            showFieldError(lblStatusError, "Status is required");
            isValid = false;
        }

        if (dpBirthDate.getValue() == null || !Validator.isValidBirthDate(dpBirthDate.getValue())) {
            showFieldError(lblBirthDateError, "Valid birth date is required");
            isValid = false;
        }

        if (tfAddress.getText().isEmpty()) {
            showFieldError(lblAddressError, "Address is required");
            isValid = false;
        } else if (tfAddress.getText().length() > 255) {
            showFieldError(lblAddressError, "Address must be 255 characters or less");
            isValid = false;
        }

        if (tfPhoneNumber.getText().isEmpty() || !tfPhoneNumber.getText().matches("\\d{8,}")) {
            showFieldError(lblPhoneError, "Valid phone number is required (at least 8 digits)");
            isValid = false;
        }

        return isValid;
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
                lblPasswordError, lblRoleError, lblStatusError, lblBirthDateError,
                lblAddressError, lblPhoneError};
        for (Label label : errorLabels) {
            if (label != null) {
                label.setVisible(false);
            }
        }
    }

    // Navigation Handlers
    @FXML
    private void handleDashboard() {
        if (checkUnsavedChanges()) {
            styleActiveButton(btnDashboard);
            if (mainApp != null) {
                mainApp.showAdminDashboardScene();
            }
        }
    }

    @FXML
    private void handleManageUsers() {
        if (checkUnsavedChanges()) {
            styleActiveButton(btnManageUsers);
            if (mainApp != null) {
                mainApp.showManageUserDashboardScene();
            }
        }
    }

    @FXML
    private void handleProducts() {
        if (checkUnsavedChanges()) {
            styleActiveButton(btnProducts);
            showComingSoon("Products Management");
        }
    }

    @FXML
    private void handleEvents() {
        if (checkUnsavedChanges()) {
            styleActiveButton(btnEvents);
            showComingSoon("Events Management");
        }
    }

    @FXML
    private void handleBlog() {
        if (checkUnsavedChanges()) {
            styleActiveButton(btnBlog);
            showComingSoon("Blog Management");
        }
    }

    @FXML
    private void handleReports() {
        if (checkUnsavedChanges()) {
            styleActiveButton(btnReports);
            if (checkUnsavedChanges()) {
                styleActiveButton(btnManageUsers);
                if (mainApp != null) {
                    mainApp.showReportsScene();
                }
            }
        }
    }

    @FXML
    private void handleSettings() {
        if (checkUnsavedChanges()) {
            styleActiveButton(btnSettings);
            showComingSoon("Settings");
        }
    }

    @FXML
    private void handleLogout() {
        if (checkUnsavedChanges()) {
            if (mainApp != null) {
                mainApp.showLoginScene();
            }
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        if (checkUnsavedChanges()) {
            handleManageUsers();
        }
    }

    private boolean checkUnsavedChanges() {
        if (hasFormChanges()) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Unsaved Changes");
            confirm.setHeaderText("You have unsaved changes");
            confirm.setContentText("Do you want to discard your changes?");

            return confirm.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .isPresent();
        }
        return true;
    }

    private boolean hasFormChanges() {
        return !tfNom.getText().isEmpty() ||
                !tfPrenom.getText().isEmpty() ||
                !tfEmail.getText().isEmpty() ||
                !tfPassword.getText().isEmpty() ||
                !tfAddress.getText().isEmpty() ||
                !tfPhoneNumber.getText().isEmpty() ||
                cbRole.getValue() != null ||
                cbStatus.getValue() != null ||
                dpBirthDate.getValue() != null;
    }

    private void showComingSoon(String feature) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Coming Soon");
        alert.setHeaderText(null);
        alert.setContentText(feature + " feature is coming soon!");
        alert.showAndWait();
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

    public void setUsername(String email) {
        if (lblUsername != null) {
            lblUsername.setText("Welcome, " + email);
        }
    }
}

