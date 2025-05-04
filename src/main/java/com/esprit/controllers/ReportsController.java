package com.esprit.controllers;

import com.esprit.models.User;
import com.esprit.models.UserStatus;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.util.List;

public class ReportsController {

    private MainprogGUI mainApp;
    private final UserService userService = new UserService() {
        @Override
        public void add(User user) {
            // No implementation needed here
        }
    };

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> colNom;
    @FXML private TableColumn<User, String> colPrenom;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colAddress;
    @FXML private TableColumn<User, String> colPhoneNumber;
    @FXML private TableColumn<User, UserStatus> colStatus;

    @FXML private Button btnConfirmUser;

    private ObservableList<User> nonConfirmedUsers = FXCollections.observableArrayList();
    @FXML
    private Button btnClaims;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadNonConfirmedUsers();
        styleButtons();
    }

    @FXML private Button btnBanUser;

    private void styleButtons() {
        if (btnConfirmUser != null) {
            btnConfirmUser.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;"); // green
        }
        if (btnBanUser != null) {
            btnBanUser.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;"); // red
        }
    }

    private void setupTableColumns() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadNonConfirmedUsers() {
        List<User> allUsers = userService.get();
        nonConfirmedUsers.clear();
        for (User user : allUsers) {
            if (user.getStatus() == UserStatus.NON_CONFIRMED) {
                nonConfirmedUsers.add(user);
            }
        }
        userTable.setItems(nonConfirmedUsers);
    }

    @FXML
    private void handleConfirmUser(ActionEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showErrorAlert("No Selection", "Please select a user to confirm.");
            return;
        }

        try {
            selectedUser.setStatus(UserStatus.CONFIRMED);
            userService.update(selectedUser);
            showSuccessAlert("User Confirmed", "User status has been updated to confirmed.");
            loadNonConfirmedUsers();
        } catch (Exception e) {
            showErrorAlert("Error", "Failed to update user status. Please try again.");
        }
    }

    @FXML
    private void handleBanUser(ActionEvent event) {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showErrorAlert("No Selection", "Please select a user to ban.");
            return;
        }

        try {
            selectedUser.setStatus(UserStatus.BANNED);
            userService.update(selectedUser);
            showSuccessAlert("User Banned", "User status has been updated to banned.");
            loadNonConfirmedUsers();
        } catch (Exception e) {
            showErrorAlert("Error", "Failed to update user status. Please try again.");
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

    public void setMainApp(MainprogGUI mainApp) {
        this.mainApp = mainApp;
    }

    public void handleDashboard(ActionEvent actionEvent) {
        handleDashboard();
    }

    public void handleManageUsers(ActionEvent actionEvent) {
        handleManageUsers();
    }

    public void handleProducts(ActionEvent actionEvent) {
        handleProducts();
    }

    public void handleEvents(ActionEvent actionEvent) {
        handleEvents();
    }

    public void handleBlog(ActionEvent actionEvent) {
        handleBlog();
    }

    public void handleReports(ActionEvent actionEvent) {
        handleReports();
    }

    public void handleSettings(ActionEvent actionEvent) {
        handleSettings();
    }

    public void handleLogout(ActionEvent actionEvent) {
        handleLogout();
    }

    // Navigation handlers for sidebar buttons

    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnManageUsers;
    @FXML
    private Button btnProducts;
    @FXML
    private Button btnEvents;
    @FXML
    private Button btnBlog;
    @FXML
    private Button btnReports;
    @FXML
    private Button btnSettings;
    @FXML
    private Button btnLogout;

    @FXML
    private void handleDashboard() {
        styleActiveButton(btnDashboard);
        if (mainApp != null) {
            mainApp.showAdminDashboardScene();
        }
    }

    @FXML
    private void handleManageUsers() {
        styleActiveButton(btnManageUsers);
        if (mainApp != null) {
            mainApp.showManageUserDashboardScene();
        }
    }

    @FXML
    private void handleProducts() {
        styleActiveButton(btnProducts);
        // TODO: Implement products functionality
        showNotImplementedAlert("Products");
    }

    @FXML
    private void handleEvents() {
        styleActiveButton(btnEvents);
        // TODO: Implement events functionality
        showNotImplementedAlert("Events");
    }

    @FXML
    private void handleBlog() {
        styleActiveButton(btnBlog);
        // TODO: Implement blog functionality
        showNotImplementedAlert("Blog");
    }

    @FXML
    private void handleReports() {
        styleActiveButton(btnReports);
        // Already on Reports page
    }

    @FXML
    private void handleSettings() {
        styleActiveButton(btnSettings);
        // TODO: Implement settings functionality
        showNotImplementedAlert("Settings");
    }

    @FXML
    private void handleLogout() {
        if (mainApp != null) {
            mainApp.showLoginScene();
        }
    }

    private void styleActiveButton(Button activeButton) {
        btnDashboard.getStyleClass().remove("selected");
        btnManageUsers.getStyleClass().remove("selected");
        btnProducts.getStyleClass().remove("selected");
        btnEvents.getStyleClass().remove("selected");
        btnBlog.getStyleClass().remove("selected");
        btnReports.getStyleClass().remove("selected");
        btnSettings.getStyleClass().remove("selected");

        if (activeButton != null) {
            activeButton.getStyleClass().add("selected");
        }
    }

    private void showNotImplementedAlert(String featureName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Not Implemented");
        alert.setHeaderText(null);
        alert.setContentText(featureName + " feature is not implemented yet.");
        alert.showAndWait();
    }

    public void handleClaims(ActionEvent actionEvent) {
        styleActiveButton(btnClaims);
        if (mainApp != null) {
            mainApp.showClaimsScene();
        } else {
            System.err.println("Error: mainApp is null in handleClaims");
        }
    }
}
