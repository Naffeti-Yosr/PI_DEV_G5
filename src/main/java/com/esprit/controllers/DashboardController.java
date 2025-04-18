package com.esprit.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import com.esprit.models.Bin;
import com.esprit.models.Truck;
import com.esprit.services.BinService;
import com.esprit.services.TruckService;
import com.esprit.tests.MainProgGUI;

import java.io.IOException;

public class DashboardController {
    // Bins Table
    @FXML private TableView<Bin> binsTable;
    @FXML private TableColumn<Bin, Integer> idBinColumn;
    @FXML private TableColumn<Bin, String> locationColumn;
    @FXML private TableColumn<Bin, String> typeColumn;
    @FXML private TableColumn<Bin, Double> fillColumn;
    @FXML private TableColumn<Bin, String> statusColumn;
    @FXML private TableColumn<Bin, Void> actionsBinColumn;

    // Trucks Table
    @FXML private TableView<Truck> trucksTable;
    @FXML private TableColumn<Truck, Integer> idTruckColumn;
    @FXML private TableColumn<Truck, Double> capacityColumn;
    @FXML private TableColumn<Truck, Double> truckFillColumn;
    @FXML private TableColumn<Truck, String> sectionColumn;
    @FXML private TableColumn<Truck, String> truckStatusColumn;
    @FXML private TableColumn<Truck, Void> actionsTruckColumn;


    private final BinService binService = new BinService();
    private final TruckService truckService = new TruckService();

    @FXML
    public void initialize() {
        // Initialize factories

        // Setup tables
        setupBinsTable();
        setupTrucksTable();

        // Load data
        refreshData();
    }

    private void setupBinsTable() {
        idBinColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("typeDechet"));
        fillColumn.setCellValueFactory(new PropertyValueFactory<>("niveauRemplissage"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        actionsBinColumn.setCellFactory(createBinActionsFactory());
    }

    private void setupTrucksTable() {
        idTruckColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capaciteMax"));
        truckFillColumn.setCellValueFactory(new PropertyValueFactory<>("niveauRemplissageActuel"));
        sectionColumn.setCellValueFactory(new PropertyValueFactory<>("section"));
        truckStatusColumn.setCellValueFactory(new PropertyValueFactory<>("statut"));
        actionsTruckColumn.setCellFactory(createTruckActionsFactory());
    }


    private Callback<TableColumn<Bin, Void>, TableCell<Bin, Void>> createBinActionsFactory() {
        return param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox buttons = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.getStyleClass().add("button-edit");
                deleteBtn.getStyleClass().add("button-delete");

                editBtn.setOnAction(event -> {
                    Bin bin = getTableView().getItems().get(getIndex());
                    editBin(bin); // Assurez-vous que cette méthode est appelée
                });

                deleteBtn.setOnAction(event -> {
                    Bin bin = getTableView().getItems().get(getIndex());
                    deleteBin(bin);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
            }
        };
    }

    private Callback<TableColumn<Truck, Void>, TableCell<Truck, Void>> createTruckActionsFactory() {
        return param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox buttons = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.getStyleClass().add("button-edit");
                deleteBtn.getStyleClass().add("button-delete");

                editBtn.setOnAction(event -> {
                    Truck truck = getTableView().getItems().get(getIndex());
                    editTruck(truck);
                });

                deleteBtn.setOnAction(event -> {
                    Truck truck = getTableView().getItems().get(getIndex());
                    deleteTruck(truck);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        };
    }

    // Navigation methods
    @FXML
    private void goToAddBin() {
        try {
            MainProgGUI.showBinForm();
            refreshBinsData();
        } catch (IOException e) {
            showAlert("Error", "Cannot open bin form: " + e.getMessage());
        }
    }

    @FXML
    private void goToAddTruck() {
        try {
            MainProgGUI.showTruckForm();
            refreshTrucksData();
        } catch (IOException e) {
            showAlert("Error", "Cannot open truck form: " + e.getMessage());
        }
    }

    // CRUD operations
    private void editBin(Bin bin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Bin-form.fxml"));
            Parent root = loader.load();

            BinFormController controller = loader.getController();
            controller.setBinData(bin); // Vérifiez que cette méthode existe dans BinFormController

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Bin");
            stage.showAndWait();

            refreshBinsData(); // Rafraîchir les données après édition
        } catch (IOException e) {
            showAlert("Error", "Failed to open bin form: " + e.getMessage());
            e.printStackTrace(); // Ajoutez ceci pour le débogage
        }
    }

    private void editTruck(Truck truck) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Truck-form.fxml"));
            Parent root = loader.load();

            TruckFormController controller = loader.getController();
            controller.setTruckData(truck);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Edit Truck");
            stage.showAndWait();

            refreshTrucksData();
        } catch (IOException e) {
            showAlert("Error", "Failed to open truck form: " + e.getMessage());
        }
    }

    private void deleteBin(Bin bin) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Bin");
        alert.setContentText("Are you sure you want to delete this bin?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                binService.delete(bin);
                refreshBinsData();
            }
        });
    }

    private void deleteTruck(Truck truck) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Truck");
        alert.setContentText("Are you sure you want to delete this truck?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                truckService.delete(truck);
                refreshTrucksData();
            }
        });
    }

    // Data refresh methods
    private void refreshData() {
        refreshBinsData();
        refreshTrucksData();
    }

    private void refreshBinsData() {
        binsTable.getItems().setAll(binService.get());
    }

    private void refreshTrucksData() {
        trucksTable.getItems().setAll(truckService.get());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}