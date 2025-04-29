package com.esprit.controllers;

import com.esprit.models.User;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;

public class ManageUserDashboardController {

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> colNom;
    @FXML private TableColumn<User, String> colPrenom;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colAddress;
    @FXML private TableColumn<User, String> colPhoneNumber;
    @FXML private TableColumn<User, String> colStatus;
    @FXML private TextField tfSearch;
    @FXML private Button btnAddUserManageDashboard;
    @FXML private Button btnModifyUser;
    @FXML private Button btnDeleteUser;
    
    private MainprogGUI mainApp;
    private final UserService userService;

    public ManageUserDashboardController() {
        this.userService = new UserService() {
            @Override
            public void add(User user) {

            }
        };
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing ManageUserDashboardController...");
        setupTableColumns();
        setupSearch();
        setupButtons();
        loadUsers();
        System.out.println("Initialization complete.");
    }
    
    private void loadUsers() {
        try {
            var users = userService.get();
            System.out.println("Loaded users count: " + users.size());
            for (User user : users) {
                System.out.println("User loaded: " + user.getNom() + " " + user.getPrenom() + " - " + user.getRole());
            }
            userTable.setItems(FXCollections.observableArrayList(users));
            userTable.refresh();
            
            if (users.isEmpty()) {
                Label placeholder = new Label("No users found in database");
                userTable.setPlaceholder(placeholder);
            } else {
                userTable.setPlaceholder(null);
            }
        } catch (Exception e) {
            System.err.println("Error loading users: " + e.getMessage());
            e.printStackTrace();
            showError("Loading Error", "Failed to load users: " + e.getMessage());
            
            Label placeholder = new Label("Error loading users");
            userTable.setPlaceholder(placeholder);
        }
    }

    private void setupTableColumns() {
        colNom.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getNom() != null ? data.getValue().getNom() : ""));
            
        colPrenom.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getPrenom() != null ? data.getValue().getPrenom() : ""));
            
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getEmail() != null ? data.getValue().getEmail() : ""));
            
        colRole.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getRole() != null ? data.getValue().getRole().getValue() : ""));
            
        colAddress.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getAddress() != null ? data.getValue().getAddress() : ""));
            
        colPhoneNumber.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getPhoneNumber() != null ? data.getValue().getPhoneNumber() : ""));
            
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getStatus() != null ? data.getValue().getStatus().toString() : ""));

        userTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            btnModifyUser.setDisable(newSelection == null);
            btnDeleteUser.setDisable(newSelection == null);
        });
    }

    private void setupSearch() {
        tfSearch.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                loadUsers();
            } else {
                searchUsers(newValue);
            }
        });
    }

    private void setupButtons() {
        btnModifyUser.setDisable(true);
        btnDeleteUser.setDisable(true);
    }

    private void searchUsers(String keyword) {
        try {
            var users = userService.search(keyword);
            userTable.setItems(FXCollections.observableArrayList(users));
            
            if (users.isEmpty()) {
                Label placeholder = new Label("No matching users found");
                userTable.setPlaceholder(placeholder);
            }
        } catch (Exception e) {
            System.err.println("Error searching users: " + e.getMessage());
            e.printStackTrace();
            showError("Search Error", "Failed to search users: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddUser() {
        if (mainApp != null) {
            System.out.println("Loading Add User scene in the same stage...");
            mainApp.loadSceneInPrimaryStage("AdminAddUser.fxml", "Add New User");
            loadUsers(); // Refresh the table after adding
        }
    }

    @FXML
    private void handleModifyUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null && mainApp != null) {
            System.out.println("Opening Modify User window for user: " + selectedUser.getNom());
            mainApp.showAdminModifyUserScene(selectedUser);
            loadUsers(); // Refresh the table after modification
        }
    }

    @FXML
    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete User");
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText("Are you sure you want to delete " + selectedUser.getNom() + "?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        userService.delete(selectedUser);
                        loadUsers();
                    } catch (Exception e) {
                        showError("Delete Error", "Could not delete user: " + e.getMessage());
                    }
                }
            });
        }
    }

    @FXML
    private void handleDashboard() {
        if (mainApp != null) {
            mainApp.showAdminDashboardScene();
        }
    }

    @FXML
    private void handleProducts() {
        showInfo("Products", "Products management coming soon!");
    }

    @FXML
    private void handleEvents() {
        showInfo("Events", "Events management coming soon!");
    }

    @FXML
    private void handleBlog() {
        showInfo("Blog", "Blog management coming soon!");
    }

@FXML
private void handleReports() {
    if (mainApp != null) {
        mainApp.loadSceneInPrimaryStage("Reports.fxml", "Reports");
    } else {
        showInfo("Reports", "Reports feature coming soon!");
    }
}

    @FXML
    private void handleSettings() {
        showInfo("Settings", "Settings panel coming soon!");
    }

    @FXML
    private void handleLogout() {
        if (mainApp != null) {
            mainApp.showLoginScene();
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setMainApp(MainprogGUI mainApp) {
        this.mainApp = mainApp;
        System.out.println("MainApp reference set in ManageUserDashboardController");
    }

    public void refreshView() {
        loadUsers();
    }

    @FXML
    public void handleManageUsers(ActionEvent actionEvent) {
        // We're already on the manage users page, just refresh the view
        refreshView();
    }
}
