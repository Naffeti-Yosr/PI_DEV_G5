package com.esprit.controllers;

import com.esprit.models.User;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class AdminAddUserController {

    private MainprogGUI mainApp;
    private final UserService userService = new UserService();

    @FXML
    private Button btnAddUser;

    @FXML
    private Button btnCancel;

    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfNom;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextField tfPrenom;

    public void setMainApp(MainprogGUI mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        cbRole.setItems(FXCollections.observableArrayList(
                "admin", "user", "BlogerAdmin", "EventPlaner", "ProductOwner", "truck driver"
        ));

        btnAddUser.setOnAction(this::handleAddUser);
        btnCancel.setOnAction(this::handleCancel);
    }

    private void handleAddUser(ActionEvent event) {
        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        String email = tfEmail.getText();
        String password = tfPassword.getText();
        String role = cbRole.getValue();
        java.time.LocalDate birthDate = dpBirthDate.getValue();

        if (nom == null || nom.isEmpty() ||
                prenom == null || prenom.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty() ||
                role == null || role.isEmpty() ||
                birthDate == null) {

            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please fill in all fields.");
            return;
        }

        User newUser = new User();
        newUser.setNom(nom);
        newUser.setPrenom(prenom);
        newUser.setEmail(email);
        newUser.setPassword(password);
        newUser.setRole(role);
        newUser.setBirth_date(java.sql.Date.valueOf(birthDate));

        userService.add(newUser);

        showAlert(Alert.AlertType.INFORMATION, "User Added", "User has been added successfully.");
        clearForm();
    }

    private void handleCancel(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showManageUserDashboardScene();
        }
    }

    private void clearForm() {
        tfNom.clear();
        tfPrenom.clear();
        tfEmail.clear();
        tfPassword.clear();
        cbRole.getSelectionModel().clearSelection();
        dpBirthDate.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void addUser(ActionEvent actionEvent) {
    }

    public void handleLogout(ActionEvent actionEvent) {
    }
}
