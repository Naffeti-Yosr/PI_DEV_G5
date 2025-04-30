package com.esprit.controllers;

import com.esprit.models.User;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;

public class AdminDashboardController {

    private MainprogGUI mainApp;

    @FXML private Button btnDashboard;
    @FXML private Button btnManageUsers;
    @FXML private Button btnProducts;
    @FXML private Button btnEvents;
    @FXML private Button btnBlog;
    @FXML private Button btnReports;
    @FXML private Button btnSettings;
    @FXML private Button btnLogout;
    @FXML private Label lblUsername;

    @FXML private Button btnNotificationBell;
    @FXML private Label lblNotificationCount;
    
    // New dashboard elements
    @FXML private Label lblTotalUsers;
    @FXML private Label lblActiveUsers;
    @FXML private Label lblNewUsers;
    @FXML private Label lblBannedUsers;
    private UserService userService;

    public void setMainApp(MainprogGUI mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        setupTooltips();
        setupButtonActions();
        setupDashboardStats();
        updateNotificationCount();
        styleActiveButton(btnDashboard); // Set dashboard as active by default
    }

    private void updateNotificationCount() {
        if (userService == null) {
            userService = new UserService() {
                @Override
                public void add(User user) {
                }
            };
        }
        int newUserCount = userService.getNewUsers();
        if (lblNotificationCount != null) {
            lblNotificationCount.setText(String.valueOf(newUserCount));
            lblNotificationCount.setVisible(newUserCount > 0);
        }
    }

    private void setupTooltips() {
        btnDashboard.setTooltip(new Tooltip("View dashboard overview"));
        btnManageUsers.setTooltip(new Tooltip("Add, edit, or remove users"));
        btnProducts.setTooltip(new Tooltip("Manage products"));
        btnEvents.setTooltip(new Tooltip("Manage events"));
        btnBlog.setTooltip(new Tooltip("Manage blog posts"));
        btnReports.setTooltip(new Tooltip("View system reports"));
        btnSettings.setTooltip(new Tooltip("Configure system settings"));
        btnLogout.setTooltip(new Tooltip("Logout from the system"));
    }

    private void setupButtonActions() {
        btnDashboard.setOnAction(event -> handleDashboard(event));
        btnManageUsers.setOnAction(event -> handleManageUsers(event));
        btnProducts.setOnAction(event -> handleProducts(event));
        btnEvents.setOnAction(event -> handleEvents(event));
        btnBlog.setOnAction(event -> handleBlog(event));
        btnReports.setOnAction(event -> handleReports(event));
        btnSettings.setOnAction(event -> handleSettings(event()));
        btnNotificationBell.setOnAction(event -> handleNotificationBellClick());
    }

    private ActionEvent event() {
        return new ActionEvent();
    }

    @FXML
    private void handleProducts(ActionEvent event) {
        styleActiveButton(btnProducts);
        // TODO: Implement products functionality
    }

    @FXML
    private void handleEvents(ActionEvent event) {
        styleActiveButton(btnEvents);
        // TODO: Implement events functionality
    }

    @FXML
    private void handleBlog(ActionEvent event) {
        styleActiveButton(btnBlog);
        // TODO: Implement blog functionality
    }

    private void setupDashboardStats() {
        if (userService == null) {
            userService = new UserService() {
                @Override
                public void add(User user) {

                }
            };
        }
        
        int totalUsers = userService.getTotalUsers();
        int activeUsers = userService.getActiveUsers();
        int newUsers = userService.getNewUsers();
        int bannedUsers = userService.getBannedUsers();
        
        updateDashboardStats(totalUsers, activeUsers, newUsers, bannedUsers);
    }

    private void updateDashboardStats(int total, int active, int newUsers, int bannedUsers) {
        if (lblTotalUsers != null) lblTotalUsers.setText(String.valueOf(total));
        if (lblActiveUsers != null) lblActiveUsers.setText(String.valueOf(active));
        if (lblNewUsers != null) lblNewUsers.setText(String.valueOf(newUsers));
        if (lblBannedUsers != null) lblBannedUsers.setText(String.valueOf(bannedUsers));
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
    private void handleDashboard(ActionEvent event) {
        styleActiveButton(btnDashboard);
        // Refresh dashboard stats
        setupDashboardStats();
    }

    @FXML
    private void handleManageUsers(ActionEvent event) {
        styleActiveButton(btnManageUsers);
        if (mainApp != null) {
            mainApp.showManageUserDashboardScene();
        }
    }

    @FXML
    private void handleReports(ActionEvent event) {
        styleActiveButton(btnReports);
        try {
            if (mainApp != null) {
                mainApp.loadSceneInPrimaryStage("Reports.fxml", "Reports");
            } else {
                System.err.println("Error: mainApp is null in handleReports");
            }
        } catch (Exception e) {
            System.err.println("Exception occurred while navigating to Reports.fxml:");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSettings(ActionEvent event) {
        styleActiveButton(btnSettings);
        // TODO: Implement settings functionality
    }

    @FXML
    void handleLogout(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showLoginScene();
        }
    }

    @FXML
    void handleAddUser(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showAdminAddUserScene();
        }
    }

    public void setUsername(String email) {
        if (lblUsername != null) {
            if (userService == null) {
                userService = new UserService() {
                    @Override
                    public void add(User user) {
                    }
                };
            }
            
            try {
                User user = userService.getUserByEmail(email);
                if (user != null && user.getNom() != null && user.getPrenom() != null) {
                    lblUsername.setText(user.getPrenom() + " " + user.getNom());
                } else {
                    lblUsername.setText("User");
                }
            } catch (Exception e) {
                lblUsername.setText("User");
            }
        }
    }

    public void refreshDashboard() {
        setupDashboardStats();
        updateNotificationCount();
    }

    @FXML
    private void handleNotificationBellClick() {
        handleReports(new ActionEvent());
    }
}
