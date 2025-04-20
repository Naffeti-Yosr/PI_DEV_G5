package com.esprit.controllers;

import com.esprit.models.User;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.ZoneId;

public class RegistrationController {

    private MainprogGUI mainApp;

    @FXML
    TextField tfNom;
    @FXML
    TextField tfPrenom;
    @FXML
    DatePicker dpBirthDate;
    @FXML
    TextField tfEmail;
    @FXML
    PasswordField tfPassword;
    @FXML
    ComboBox<String> cbRole;

    @FXML
    public void initialize() {
        cbRole.getItems().clear();
        cbRole.getItems().addAll("admin", "user", "BlogerAdmin", "EventPlaner", "ProductOwner", "truck driver");
    }

    @FXML
    void addPerson(ActionEvent event) {
        UserService userService = new UserService();

        // Validation
        if (tfNom.getText().isEmpty() || tfPrenom.getText().isEmpty() ||
                tfEmail.getText().isEmpty() || tfPassword.getText().isEmpty() ||
                cbRole.getValue() == null || dpBirthDate.getValue() == null) {

            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields!");
            return;
        }

        try {
            java.util.Date birthDate = java.util.Date.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            User user = new User(
                    0,
                    tfNom.getText(),
                    tfPrenom.getText(),
                    birthDate,
                    tfEmail.getText(),
                    tfPassword.getText(),
                    cbRole.getValue()
            );

            userService.add(user);
            showAlert(Alert.AlertType.INFORMATION, "Success", "User added successfully!");
            clearFields();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to add user: " + e.getMessage());
        }
    }

    @FXML
    void handleBackToLogin(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showLoginScene();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Main application reference is missing!");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    private void clearFields() {
        tfNom.clear();
        tfPrenom.clear();
        dpBirthDate.setValue(null);
        tfEmail.clear();
        tfPassword.clear();
        cbRole.setValue(null);
    }
    public void setEditMode(User user) {
        if (user != null) {
            tfNom.setText(user.getNom());
            tfPrenom.setText(user.getPrenom());
            if (user.getBirth_date() != null) {
                dpBirthDate.setValue(user.getBirth_date().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate());
            }
            tfEmail.setText(user.getEmail());
            tfPassword.setText(user.getPassword());
            cbRole.setValue(user.getRole());
        }
    }

    public void setMainApp(MainprogGUI mainApp) {
        this.mainApp = mainApp;
    }
}
