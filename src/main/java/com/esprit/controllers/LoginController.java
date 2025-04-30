package com.esprit.controllers;

import com.esprit.models.User;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import com.esprit.utils.RememberMeUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class LoginController {
    private MainprogGUI mainApp;

    @FXML
    private TextField tfEmail;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private CheckBox cbRememberMe;

    public void setMainApp(MainprogGUI mainApp) {
        this.mainApp = mainApp;
        loadRememberedUser();
    }

    private void loadRememberedUser() {
        String savedEmail = RememberMeUtil.getSavedEmail();
        String savedPassword = RememberMeUtil.getSavedPassword();
        if (savedEmail != null) {
            tfEmail.setText(savedEmail);
            cbRememberMe.setSelected(true);
        }
        if (savedPassword != null) {
            tfPassword.setText(savedPassword);
        }
    }

    @FXML
    void handleForgetPassword(ActionEvent event) {
        System.out.println("handleForgetPassword called");
        if (mainApp != null) {
            System.out.println("mainApp is not null, navigating to ForgetPasswordScene");
            mainApp.showForgetPasswordScene();
        } else {
            System.out.println("mainApp is null in handleForgetPassword");
            showAlert("Error", "Application reference missing");
        }
    }

    @FXML
    void handleLogin(ActionEvent event) {
        if (tfEmail.getText().isEmpty() || tfPassword.getText().isEmpty()) {
            showAlert("Error", "Please fill in all fields");
            return;
        }

        UserService userService = new UserService() {
            @Override
            public void add(User user) {

            }
        };
        try {
            User user = userService.authenticate(tfEmail.getText(), tfPassword.getText());
            if (user != null) {
                System.out.println("Login successful!");
                if (mainApp != null) {
                    System.setProperty("currentUser", user.getEmail());
                    if (cbRememberMe.isSelected()) {
                        RememberMeUtil.saveEmail(user.getEmail());
                        RememberMeUtil.savePassword(tfPassword.getText());
                    } else {
                        RememberMeUtil.clearAll();
                    }
                    mainApp.showAdminDashboardScene();
                }
            } else {
                showAlert("Error", "Invalid email or password");
            }
        } catch (Exception e) {
            showAlert("Error", "Login failed: " + e.getMessage());
        }
    }

    @FXML
    void handleRegister(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showRegisterScene();
        } else {
            showAlert("Error", "Application reference missing");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
