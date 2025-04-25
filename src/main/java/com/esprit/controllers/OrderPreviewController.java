package com.esprit.controllers;

import com.esprit.models.Commande;
import com.esprit.models.User;
import com.esprit.models.CommandeProduit;
import com.esprit.Services.UserService;
import com.esprit.Services.CommandeService;
import com.esprit.Services.CommandeProduitService;
import com.esprit.Services.ProduitService;
import com.esprit.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderPreviewController {

    @FXML private Label idLabel;
    @FXML private Label dateLabel;
    @FXML private ComboBox<String> statusCombo;
    @FXML private Label clientLabel;

    @FXML private TableView<CommandeProduit> itemsTable;
    @FXML private TableColumn<CommandeProduit, String> productNameColumn;
    @FXML private TableColumn<CommandeProduit, Integer> quantityColumn;
    @FXML private TableColumn<CommandeProduit, Double> totalColumn;

    private Commande order;
    private final UserService userService = new UserService();
    private final CommandeService commandeService = new CommandeService();
    private final CommandeProduitService commandeProduitService = new CommandeProduitService();
    private final ProduitService produitService = new ProduitService();
    private final Map<Integer, User> userMap = new HashMap<>();

    @FXML
    public void initialize() {
        statusCombo.getItems().setAll("Pending", "Processing", "Completed", "Cancelled");

        List<User> users = userService.recuperer();
        if (users != null) {
            userMap.putAll(users.stream()
                .collect(Collectors.toMap(User::getId, u -> u)));
        }

        productNameColumn.setCellValueFactory(cellData -> {
            int productId = cellData.getValue().getProduitId();
            String productName = produitService.getProduitById(productId).getNom();
            return new javafx.beans.property.SimpleStringProperty(productName);
        });

        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("sousTotal"));
    }

    public void setOrder(Commande order) {
        this.order = order;
        displayOrderDetails();
        loadOrderItems();
    }

    private void displayOrderDetails() {
        idLabel.setText(String.valueOf(order.getId()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = (order.getDate() != null) ? sdf.format(order.getDate()) : "N/A";
        dateLabel.setText(formattedDate);

        String orderStatus = order.getStatut();
        if (orderStatus != null && statusCombo.getItems().contains(orderStatus)) {
            statusCombo.setValue(orderStatus);
        } else {
            System.out.println("Invalid status: " + orderStatus + " — setting default");
            statusCombo.setValue("Pending");
        }

        User client = userMap.get(order.getClientId());
        if (client != null) {
            clientLabel.setText(client.getPrenom() + " " + client.getNom());
        } else {
            clientLabel.setText("Unknown");
        }
    }

    private void loadOrderItems() {
        if (order == null) {
            return;
        }
        List<CommandeProduit> items = commandeProduitService.getOrderItemsByCommandeId(order.getId());
        ObservableList<CommandeProduit> observableItems = FXCollections.observableArrayList(items);
        itemsTable.setItems(observableItems);
    }

    @FXML
    private void handleSave() {
        boolean confirmed = UIUtils.showConfirmation("Confirm Save", "Are you sure you want to save changes?");
        if (!confirmed) {
            return;
        }
        if (order != null) {
            String newStatus = statusCombo.getValue();
            if (newStatus != null && !newStatus.equals(order.getStatut())) {
                order.setStatut(newStatus);
                commandeService.modifier(order);
            }
        }
        // Navigate back to OrderDashboard after saving
        try {
            Stage stage = (Stage) idLabel.getScene().getWindow();
            if (stage != null) {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/views/OrderDashboardView.fxml"));
                javafx.scene.Parent root = loader.load();
                stage.getScene().setRoot(root);
                stage.setTitle("Order Dashboard");
            }
        } catch (Exception e) {
            UIUtils.showAlert("Error", null, "Failed to return to dashboard: " + e.getMessage(), "ERROR");
        }
    }

    @FXML
    private void handleClose() {
        try {
            Stage stage = (Stage) idLabel.getScene().getWindow();
            if (stage != null) {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/views/OrderDashboardView.fxml"));
                javafx.scene.Parent root = loader.load();
                stage.getScene().setRoot(root);
                stage.setTitle("Order Dashboard");
            }
        } catch (Exception e) {
            UIUtils.showAlert("Error", null, "Failed to return to dashboard: " + e.getMessage(), "ERROR");
        }
    }
}
