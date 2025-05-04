package com.esprit.controllers;

import com.esprit.models.User;
import com.esprit.models.UserRole;
import com.esprit.models.UserStatus;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import com.esprit.utils.Validator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.sql.Date;

public class AdminModifyController {

    private MainprogGUI mainApp;
    private UserService userService = new UserService() {
        @Override
        public void add(User user) {

        }
    };
    private User currentUser;

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

    public void setMainApp(MainprogGUI mainApp) {
        this.mainApp = mainApp;
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
                } else if (!newValue.equals(currentUser.getEmail()) &&
                        userService.getUserByEmail(newValue) != null) {
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

    public void setUser(User user) {
        if (user == null) return;

        this.currentUser = user;

        tfNom.setText(user.getNom() != null ? user.getNom() : "");
        tfPrenom.setText(user.getPrenom() != null ? user.getPrenom() : "");
        tfEmail.setText(user.getEmail() != null ? user.getEmail() : "");
        tfAddress.setText(user.getAddress() != null ? user.getAddress() : "");
        tfPhoneNumber.setText(user.getPhoneNumber() != null ? user.getPhoneNumber() : "");
        cbRole.setValue(user.getRole());
        cbStatus.setValue(user.getStatus());

        // Convert java.sql.Date to LocalDate
        if (user.getBirth_date() != null) {
            dpBirthDate.setValue(((Date) user.getBirth_date()).toLocalDate());
        }

        tfPassword.clear();
        tfPassword.setPromptText("Enter new password to change");
    }

    @FXML
    private void handleSave(ActionEvent event) {
        clearErrorLabels();

        if (!validateForm()) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Changes");
        confirm.setHeaderText("Save Changes");
        confirm.setContentText("Are you sure you want to save these changes?");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                saveChanges();
            }
        });
    }

    private void saveChanges() {
        try {
            updateUserFromForm();
            userService.update(currentUser);
            showSuccessAlert("Success", "User updated successfully");
            closeCurrentWindow();
            handleManageUsers();
        } catch (Exception e) {
            showErrorAlert("Error", "Could not update user: " + e.getMessage());
        }
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
        } else if (!tfEmail.getText().equals(currentUser.getEmail()) &&
                userService.getUserByEmail(tfEmail.getText()) != null) {
            showFieldError(lblEmailError, "This email is already in use");
            isValid = false;
        }

        if (!tfPassword.getText().isEmpty() && !Validator.isValidPassword(tfPassword.getText())) {
            showFieldError(lblPasswordError, "Password must meet security requirements");
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
        }

        if (tfPhoneNumber.getText().isEmpty() || !tfPhoneNumber.getText().matches("\\d{8,}")) {
            showFieldError(lblPhoneError, "Valid phone number is required (at least 8 digits)");
            isValid = false;
        }

        return isValid;
    }

    private void updateUserFromForm() {
        currentUser.setNom(tfNom.getText().trim());
        currentUser.setPrenom(tfPrenom.getText().trim());
        currentUser.setEmail(tfEmail.getText().trim());
        currentUser.setRole(cbRole.getValue());
        currentUser.setStatus(cbStatus.getValue());
        currentUser.setBirth_date(Date.valueOf(dpBirthDate.getValue()));
        currentUser.setAddress(tfAddress.getText().trim());
        currentUser.setPhoneNumber(tfPhoneNumber.getText().trim());

        if (!tfPassword.getText().isEmpty()) {
            currentUser.setPassword(tfPassword.getText());
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
                closeCurrentWindow();
                mainApp.showAdminDashboardScene();
            }
        }
    }

    @FXML
    private void handleManageUsers() {
        if (checkUnsavedChanges()) {
            styleActiveButton(btnManageUsers);
            if (mainApp != null) {
                closeCurrentWindow();
                mainApp.showManageUserDashboardScene();
            }
        }
    }

    @FXML
    private void handleProducts() {
        if (checkUnsavedChanges()) {
            styleActiveButton(btnProducts);
            closeCurrentWindow();
            showComingSoon("Products Management");
        }
    }

    @FXML
    private void handleEvents() {
        if (checkUnsavedChanges()) {
            styleActiveButton(btnEvents);
            closeCurrentWindow();
            showComingSoon("Events Management");
        }
    }

    @FXML
    private void handleBlog() {
        if (checkUnsavedChanges()) {
            styleActiveButton(btnBlog);
            closeCurrentWindow();
            showComingSoon("Blog Management");
        }
    }

    @FXML
    private void handleReports() {
        if (checkUnsavedChanges()) {
            styleActiveButton(btnReports);
            closeCurrentWindow();
            showComingSoon("Reports");
        }
    }

    @FXML
    private void handleSettings() {
        if (checkUnsavedChanges()) {
            styleActiveButton(btnSettings);
            closeCurrentWindow();
            showComingSoon("Settings");
        }
    }

    @FXML
    private void handleLogout() {
        if (checkUnsavedChanges()) {
            if (mainApp != null) {
                closeCurrentWindow();
                mainApp.showLoginScene();
            }
        }
    }

    @FXML
    public void handleCancel(ActionEvent event) {
        if (checkUnsavedChanges()) {
            closeCurrentWindow();
            handleManageUsers();
        }
    }

    private void closeCurrentWindow() {
        // Get the current stage from any control (using btnSave here)
        if (btnSave != null && btnSave.getScene() != null) {
            Stage stage = (Stage) btnSave.getScene().getWindow();
            if (stage != null) {
                stage.close();
            }
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
        if (currentUser == null) return false;

        LocalDate currentBirthDate = null;
        if (currentUser.getBirth_date() != null) {
            currentBirthDate = ((Date) currentUser.getBirth_date()).toLocalDate();
        }

        if (cbRole.getValue() == null || cbStatus.getValue() == null) {
            return true; // Consider form changed if role or status is not selected
        }

        return !tfNom.getText().equals(currentUser.getNom()) ||
                !tfPrenom.getText().equals(currentUser.getPrenom()) ||
                !tfEmail.getText().equals(currentUser.getEmail()) ||
                !tfAddress.getText().equals(currentUser.getAddress()) ||
                !tfPhoneNumber.getText().equals(currentUser.getPhoneNumber()) ||
                !cbRole.getValue().equals(currentUser.getRole()) ||
                !cbStatus.getValue().equals(currentUser.getStatus()) ||
                !tfPassword.getText().isEmpty() ||
                (dpBirthDate.getValue() != null && currentBirthDate != null &&
                        !dpBirthDate.getValue().equals(currentBirthDate));
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

    public void setUsername(String email) {
        if (lblUsername != null) {
            lblUsername.setText("Welcome, " + email);
        }
    }

    public void handleClaims(ActionEvent actionEvent) {
    }
}

