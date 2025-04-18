package com.esprit.controllers;

import com.esprit.models.Commande;
import com.esprit.models.Users;
import com.esprit.Services.UsersService;
import com.esprit.Services.CommandeService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
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

    private Commande order;
    private final UsersService usersService = new UsersService();
    private final CommandeService commandeService = new CommandeService();
    private final Map<Integer, Users> userMap = new HashMap<>();

    @FXML
    public void initialize() {
  statusCombo.getItems().setAll("Pending", "Processing", "Completed", "Cancelled");

  List<Users> users = usersService.recuperer();
        if (users != null) {
            userMap.putAll(users.stream()
                .collect(Collectors.toMap(Users::getId, u -> u)));
        }
    }

    public void setOrder(Commande order) {
        this.order = order;
        displayOrderDetails();
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

        Users client = userMap.get(order.getClientId());
        if (client != null) {
            clientLabel.setText(client.getPrenom() + " " + client.getNom());
        } else {
            clientLabel.setText("Unknown");
        }
    }

    @FXML
    private void handleSave() {
        if (order != null) {
            String newStatus = statusCombo.getValue();
            if (newStatus != null && !newStatus.equals(order.getStatut())) {
                order.setStatut(newStatus);
                commandeService.modifier(order);
            }
        }
        // Close the window after saving
        Stage stage = (Stage) idLabel.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    @FXML
    private void handleClose() {
        // Fermer la fenêtre
        Stage stage = (Stage) idLabel.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }
}
