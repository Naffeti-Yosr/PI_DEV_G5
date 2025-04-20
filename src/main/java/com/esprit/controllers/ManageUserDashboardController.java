package com.esprit.controllers;



import com.esprit.models.User;
import com.esprit.services.UserService;
import com.esprit.tests.MainprogGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class ManageUserDashboardController {

    private MainprogGUI mainApp;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnAddUserManageDashboard;

    @FXML
    private Button btnManageUsers;

    @FXML
    private Button btnReports;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnModifyUser;

    @FXML
    private Button btnDeleteUser;

    @FXML
    private Label lblUsername;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> colNom;

    @FXML
    private TableColumn<User, String> colPrenom;

    @FXML
    private TableColumn<User, String> colEmail;

    @FXML
    private TableColumn<User, String> colRole;

    private UserService userService = new UserService();

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfPrenom;

    @FXML
    private TextField tfEmail;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnAddUser;

    public void setMainApp(MainprogGUI mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        System.out.println("ManageUserDashboardController initialized. mainApp is " + mainApp);
        btnDashboard.setTooltip(new Tooltip("View dashboard overview"));
        btnManageUsers.setTooltip(new Tooltip("Add, edit, or remove users"));
        btnReports.setTooltip(new Tooltip("View system reports"));
        btnSettings.setTooltip(new Tooltip("Configure system settings"));
        btnLogout.setTooltip(new Tooltip("Logout from the system"));

        btnManageUsers.setOnAction(event -> {
            if (mainApp != null) {
                mainApp.showManageUserDashboardScene();
            } else {
                System.out.println("mainApp is null in btnManageUsers handler");
            }
        });

        if (btnAddUser != null) {
            cbRole.getItems().clear();
            cbRole.getItems().addAll("admin", "user", "BlogerAdmin", "EventPlaner", "ProductOwner", "truck driver");
            btnAddUser.setOnAction(event -> addUser(event));
        }

        if (btnAddUserManageDashboard != null) {
            btnAddUserManageDashboard.setOnAction(event -> {
                if (mainApp != null) {
                    System.out.println("btnAddUserManageDashboard clicked, navigating to ManageAddUserScene");
                    mainApp.showManageAddUserScene();
                } else {
                    System.out.println("mainApp is null in btnAddUserManageDashboard handler");
                }
            });
        }

        if (btnCancel != null) {
            btnCancel.setOnAction(event -> {
                if (mainApp != null) {
                    mainApp.showManageUserDashboardScene();
                }
            });
        }

        if (btnDeleteUser != null) {
            btnDeleteUser.setOnAction(event -> handleDeleteUser());
        }
        if (btnModifyUser != null) {
            btnModifyUser.setOnAction(event -> handleModifyUser());
        }

        if (userTable != null) {
            colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
            colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

            loadUsers();
        }
    }

    @FXML
    private void addUser(ActionEvent event) {
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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
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

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User Added");
        alert.setHeaderText(null);
        alert.setContentText("User has been added successfully.");
        alert.showAndWait();

        tfNom.clear();
        tfPrenom.clear();
        tfEmail.clear();
        tfPassword.clear();
        cbRole.getSelectionModel().clearSelection();
        dpBirthDate.setValue(null);

        loadUsers();
    }

    private void loadUsers() {
        List<User> users = userService.get();
        ObservableList<User> userList = FXCollections.observableArrayList(users);
        userTable.setItems(userList);
    }

    private void handleDeleteUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            userService.delete(selectedUser);
            loadUsers();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user to delete.");
            alert.showAndWait();
        }
    }

    private void handleModifyUser() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                mainApp.showAdminModifyUserScene(selectedUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText(null);
            alert.setContentText("Please select a user to modify.");
            alert.showAndWait();
        }
    }

    @FXML
    void handleLogout(ActionEvent event) {
        if (mainApp != null) {
            mainApp.showLoginScene();
        }
    }

    public void setUsername(String email) {
        User user = userService.getUserByEmail(email);
        String displayName = (user != null) ? user.getNom() + " " + user.getPrenom() : email;
        if (lblUsername != null) {
            lblUsername.setText(displayName);
        }
    }
}
