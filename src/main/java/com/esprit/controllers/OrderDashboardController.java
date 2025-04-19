package com.esprit.controllers;

import com.esprit.models.Commande;
import com.esprit.models.User;
import com.esprit.Services.CommandeService;
import com.esprit.Services.UserService;
import com.esprit.utils.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class OrderDashboardController extends BaseController<Commande> {

    @FXML private TableView<Commande> orderTable;
    @FXML private TableColumn<Commande, Integer> idColumn;
    @FXML private TableColumn<Commande, String> dateColumn;
    @FXML private TableColumn<Commande, String> statusColumn;
    @FXML private TableColumn<Commande, String> clientColumn;
    @FXML private Button previewButton;
    @FXML private ComboBox<String> clientCombo;
    @FXML private ComboBox<String> statusCombo;

    private final CommandeService commandeService = new CommandeService();
    private final UserService userService = new UserService();
    private Connection connection;

    private Map<String, Integer> clientMap = new HashMap<>();
    private ObservableList<Commande> allOrders = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.connection = DataSource.getInstance().getConnection();
            if (this.connection != null && !this.connection.isClosed()) {
                setupTableColumns();
                orderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                loadOrderData();
                setupSelectionListener();
                loadClients();
                loadStatuses();
                setupFilters();
            } else {
                showAlert("Database Error", "Failed to establish database connection");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to connect to database: " + e.getMessage());
        }
    }

    @Override
    protected ObservableList<Commande> loadData() {
        allOrders.setAll(commandeService.recuperer());
        return allOrders;
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        dateColumn.setCellValueFactory(cellData -> {
            java.util.Date date = cellData.getValue().getDate();
            String formattedDate = date != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(date) : "";
            return new javafx.beans.property.SimpleStringProperty(formattedDate);
        });

        statusColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatut()));

        clientColumn.setCellValueFactory(cellData -> {
            int clientId = cellData.getValue().getClientId();
            String clientName = clientMap.entrySet().stream()
                    .filter(entry -> entry.getValue() == clientId)
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse("Unknown");
            return new javafx.beans.property.SimpleStringProperty(clientName);
        });
    }

    private void loadOrderData() {
        allOrders.setAll(commandeService.recuperer());
        orderTable.setItems(allOrders);
    }

    private void setupSelectionListener() {
        orderTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                      previewButton.setDisable(newSelection == null);
                });
    }

    private void loadClients() {
        clientMap.clear();
        clientCombo.getItems().clear();
        clientCombo.getItems().add("All Clients");

        List<User> clients = userService.recuperer();
        for (User client : clients) {
            String fullName = client.getPrenom() + " " + client.getNom();
            clientMap.put(fullName, client.getId());
            clientCombo.getItems().add(fullName);
        }
        clientCombo.getSelectionModel().selectFirst();
    }

    private void loadStatuses() {
        statusCombo.getItems().clear();
        statusCombo.getItems().addAll("All Statuses", "Pending", "Processing", "Completed", "Cancelled");
        statusCombo.getSelectionModel().selectFirst();
    }

    private void setupFilters() {
        clientCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> applyFilters());
        statusCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> applyFilters());
    }

    private void applyFilters() {
        String selectedClient = clientCombo.getValue();
        String selectedStatus = statusCombo.getValue();

        List<Commande> filtered = allOrders.stream()
                .filter(order -> {
                    boolean clientMatches = selectedClient == null || selectedClient.equals("All Clients") ||
                            (clientMap.getOrDefault(selectedClient, -1) == order.getClientId());
                    boolean statusMatches = selectedStatus == null || selectedStatus.equals("All Statuses") ||
                            selectedStatus.equalsIgnoreCase(order.getStatut());
                    return clientMatches && statusMatches;
                })
                .collect(Collectors.toList());

        orderTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void handleDelete() {
        Commande selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Delete order ID: " + selected.getId());
            confirm.setContentText("Are you sure?");

            if (confirm.showAndWait().get() == ButtonType.OK) {
                commandeService.supprimer(selected);
                loadOrderData();
            }
        }
    }

    @FXML
    private void handlePreview() {
        Commande selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/OrderPreviewView.fxml"));
                Parent root = loader.load();

                OrderPreviewController controller = loader.getController();
                controller.setOrder(selected);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Order Preview");
                stage.showAndWait();

                // Refresh order table after preview window closes
                loadOrderData();
            } catch (Exception e) {
                showAlert("Error", "Failed to open preview: " + e.getMessage());
            }
        } else {
            showAlert("Warning", "Please select an order to preview.");
        }
    }
}
