package com.esprit.controllers;

import com.esprit.models.User;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import java.sql.Date;
import java.time.LocalDate;

public class AdminModifyController {

    private MainprogGUI mainApp;
    private UserService userService = new UserService();
    private User currentUser;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnManageUsers;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnSettings;

    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private Label lblUsername;

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
        cbRole.setItems(FXCollections.observableArrayList("admin", "user", "BlogerAdmin", "EventPlaner", "ProductOwner", "truck driver"));

        btnSave.setOnAction(event -> handleSave(event));
        btnCancel.setOnAction(event -> {
            if (mainApp != null) {
                mainApp.showManageUserDashboardScene();
            }
        });
    }

    public void setUser(User user) {
        if (user == null) return;

        this.currentUser = user;

        tfNom.setText(user.getNom() != null ? user.getNom() : "");
        tfPrenom.setText(user.getPrenom() != null ? user.getPrenom() : "");
        tfEmail.setText(user.getEmail() != null ? user.getEmail() : "");
        //dont load the password we gone force the user on chnage it
        String password = tfPassword.getText();
        cbRole.setValue(user.getRole());
        java.time.LocalDate birthDate = dpBirthDate.getValue();
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (currentUser == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No user selected for modification.");
            return;
        }

        String nom = tfNom.getText();
        String prenom = tfPrenom.getText();
        String email = tfEmail.getText();
        String password = tfPassword.getText();
        String role = cbRole.getValue();
        LocalDate birthDate = dpBirthDate.getValue();

        if (nom == null || nom.isEmpty() ||
                prenom == null || prenom.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty() ||
                role == null || role.isEmpty() ||
                birthDate == null) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "Please fill in all fields.");
            return;
        }

        currentUser.setNom(nom);
        currentUser.setPrenom(prenom);
        currentUser.setEmail(email);
        currentUser.setPassword(password);
        currentUser.setRole(role);
        currentUser.setBirth_date(Date.valueOf(birthDate));

        userService.update(currentUser);

        showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully.");

        if (mainApp != null) {
            mainApp.showManageUserDashboardScene();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }

    public void handleCancel(ActionEvent actionEvent) {
    }
}
