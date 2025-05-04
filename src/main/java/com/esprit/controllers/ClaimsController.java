package com.esprit.controllers;

import com.esprit.models.Reclamation;
import com.esprit.services.ReclamationService;
import com.esprit.tests.MainprogGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ClaimsController {

    @FXML private TableView<Reclamation> claimsTable;
    @FXML private TableColumn<Reclamation, Integer> colId;
    @FXML private TableColumn<Reclamation, String> colUserId;
    @FXML private TableColumn<Reclamation, String> colReport;
    @FXML private TableColumn<Reclamation, String> colStatus;
    @FXML private TableColumn<Reclamation, String> colCreatedAt;
    @FXML private Button btnRefresh;

    // Sidebar buttons
    @FXML private Button btnDashboard;
    @FXML private Button btnManageUsers;
    @FXML private Button btnProducts;
    @FXML private Button btnEvents;
    @FXML private Button btnBlog;
    @FXML private Button btnReports;
    @FXML private Button btnClaims;
    @FXML private Button btnSettings;
    @FXML private Button btnLogout;

    private MainprogGUI mainApp;
    private final ReclamationService reclamationService;
    private final ObservableList<Reclamation> reclamations;

    public ClaimsController() {
        this.reclamationService = new ReclamationService();
        this.reclamations = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        setupTableContextMenu();
        loadReclamations();
        btnRefresh.setOnAction(this::handleRefresh);

        // Change ID column header to "Ticket Number"
        colId.setText("Ticket Number");
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colUserId.setCellValueFactory(cellData -> {
            String fullName = cellData.getValue().getNom() + " " + cellData.getValue().getPrenom();
            return new javafx.beans.property.SimpleStringProperty(fullName);
        });
        colReport.setCellValueFactory(new PropertyValueFactory<>("report"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        // Format date in a readable format
        colCreatedAt.setCellValueFactory(cellData -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = sdf.format(cellData.getValue().getCreatedAt());
            return new javafx.beans.property.SimpleStringProperty(formattedDate);
        });

        // Add tooltip for report column to show full text
        colReport.setCellFactory(column -> {
            TableCell<Reclamation, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setTooltip(null);
                    } else {
                        setText(item);
                        Tooltip tooltip = new Tooltip(item);
                        tooltip.setWrapText(true);
                        tooltip.setMaxWidth(300);
                        setTooltip(tooltip);
                    }
                }
            };
            return cell;
        });
    }

    private void setupTableContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        
        MenuItem updateStatusItem = new MenuItem("Update Status");
        updateStatusItem.setOnAction(e -> {
            Reclamation selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
            if (selectedClaim != null) {
                showStatusUpdateDialog(selectedClaim);
            }
        });

        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> {
            Reclamation selectedClaim = claimsTable.getSelectionModel().getSelectedItem();
            if (selectedClaim != null) {
                deleteClaim(selectedClaim);
            }
        });

        contextMenu.getItems().addAll(updateStatusItem, deleteItem);
        claimsTable.setContextMenu(contextMenu);
    }

    private void showStatusUpdateDialog(Reclamation claim) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Update Status");
        dialog.setHeaderText("Update status for claim #" + claim.getId());

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Pending", "In Progress", "Resolved", "Rejected");
        statusComboBox.setValue(claim.getStatus());

        dialog.getDialogPane().setContent(statusComboBox);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                return statusComboBox.getValue();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newStatus -> {
            try {
                claim.setStatus(newStatus);
                reclamationService.updateReclamation(claim);
                loadReclamations();
            } catch (SQLException ex) {
                showErrorAlert("Update Error", "Failed to update claim status: " + ex.getMessage());
            }
        });
    }

    private void deleteClaim(Reclamation claim) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Claim");
        alert.setHeaderText("Delete Claim #" + claim.getId());
        alert.setContentText("Are you sure you want to delete this claim?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                try {
                    reclamationService.deleteReclamation(claim.getId());
                    loadReclamations();
                } catch (SQLException e) {
                    showErrorAlert("Delete Error", "Failed to delete claim: " + e.getMessage());
                }
            }
        });
    }

    private void loadReclamations() {
        try {
            List<Reclamation> list = reclamationService.getAllReclamations();
            reclamations.setAll(list);
            claimsTable.setItems(reclamations);
        } catch (SQLException e) {
            showErrorAlert("Database Error", "Failed to load claims from the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadReclamations();
    }

    private void showErrorAlert(String title, String content) {
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

    // Sidebar Navigation Methods
    @FXML
    private void handleDashboard(ActionEvent event) {
        if (mainApp != null) {
            mainApp.loadSceneInPrimaryStage("/admin_dashboard.fxml", "Admin Dashboard");
        }
    }

    @FXML
    private void handleManageUsers(ActionEvent event) {
        if (mainApp != null) {
            mainApp.loadSceneInPrimaryStage("/manage_user_dashboard.fxml", "Manage Users");
        }
    }

    @FXML
    private void handleProducts(ActionEvent event) {
        showInfo("Products", "Products management coming soon!");
    }

    @FXML
    private void handleEvents(ActionEvent event) {
        showInfo("Events", "Events management coming soon!");
    }

    @FXML
    private void handleBlog(ActionEvent event) {
        showInfo("Blog", "Blog management coming soon!");
    }

    @FXML
    private void handleReports(ActionEvent event) {
        if (mainApp != null) {
            mainApp.loadSceneInPrimaryStage("/Reports.fxml", "Reports");
        }
    }

    @FXML
    private void handleClaims(ActionEvent event) {
        // Already on Claims page
    }

    @FXML
    private void handleSettings(ActionEvent event) {
        showInfo("Settings", "Settings panel coming soon!");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        if (mainApp != null) {
            mainApp.loadSceneInPrimaryStage("/login_page.fxml", "Login");
        }
    }

    public void setMainApp(MainprogGUI mainApp) {
        this.mainApp = mainApp;
    }
}
