package com.esprit.controllers;

import com.esprit.models.User;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;

public class AddUserController {

    private MainprogGUI mainApp;

    @FXML private TextField tfNom;
    @FXML private TextField tfPrenom;
    @FXML private DatePicker dpBirthDate;
    @FXML private TextField tfEmail;
    @FXML private PasswordField tfPassword;
    @FXML private ComboBox<String> cbRole;

    @FXML
    public void initialize() {
        cbRole.getItems().addAll("admin", "user");
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
            User user = new User(
                    tfNom.getText(),
                    tfPrenom.getText(),
                    Date.valueOf(dpBirthDate.getValue()),
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

    public void setMainApp(MainprogGUI mainApp) {
        this.mainApp = mainApp;
    }
}
