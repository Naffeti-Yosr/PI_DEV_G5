package com.esprit.controllers;

import com.esprit.models.Produit;
import com.esprit.models.Categorie;
import com.esprit.Services.ProduitService;
import com.esprit.Services.CategorieService;
import com.esprit.utils.DataSource;
import com.esprit.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class ProductDashboardController extends BaseController<Produit> {

    @FXML private TableView<Produit> productTable;
    @FXML private TableColumn<Produit, String> nameColumn;
    @FXML private TableColumn<Produit, String> descriptionColumn;
    @FXML private TableColumn<Produit, Double> priceColumn;
    @FXML private TableColumn<Produit, Integer> stockColumn;
    @FXML private TableColumn<Produit, Boolean> recyclableColumn;
    @FXML private TableColumn<Produit, String> categoryColumn;
    @FXML private TableColumn<Produit, String> imageColumn;
    
    @FXML private Button updateButton;
    @FXML private Button previewButton;
    @FXML private ComboBox<String> categoryCombo;
    
    private Map<String, Integer> categoryMap = new HashMap<>();
    private final ProduitService produitService = new ProduitService();
    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.connection = DataSource.getInstance().getConnection();
            if (this.connection != null && !this.connection.isClosed()) {
                setupTableColumns();
                productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                loadProductData();
                setupSelectionListener();
                loadCategories();
            } else {
                UIUtils.showAlert("Database Error", null, "Failed to establish database connection", "ERROR");
            }
        } catch (SQLException e) {
            UIUtils.showAlert("Database Error", null, "Failed to connect to database: " + e.getMessage(), "ERROR");
        }
    }


    private void setupTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        nameColumn.setSortable(true);

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setSortable(true);

        priceColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        priceColumn.setSortable(true);

        stockColumn.setCellValueFactory(new PropertyValueFactory<>("quantiteStock"));
        stockColumn.setSortable(true);

        categoryColumn.setCellValueFactory(cellData -> {
            int categoryId = cellData.getValue().getCategorieId();
            String categoryName = produitService.getCategoryNameById(categoryId);
            return new javafx.beans.property.SimpleStringProperty(categoryName);
        });
        categoryColumn.setSortable(true);

        recyclableColumn.setCellValueFactory(new PropertyValueFactory<>("recyclable"));
        recyclableColumn.setSortable(true);

        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        imageColumn.setSortable(true);

        productTable.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double tableWidth = newWidth.doubleValue();
            nameColumn.setPrefWidth(tableWidth * 0.14);         
            descriptionColumn.setPrefWidth(tableWidth * 0.22);  
            priceColumn.setPrefWidth(tableWidth * 0.12);        
            stockColumn.setPrefWidth(tableWidth * 0.10);        
            categoryColumn.setPrefWidth(tableWidth * 0.12);     
            recyclableColumn.setPrefWidth(tableWidth * 0.10);   
            imageColumn.setPrefWidth(tableWidth * 0.20);        
        });
        
        recyclableColumn.setCellFactory(column -> new TableCell<Produit, Boolean>() {
            private final Label badge = new Label();
            {
                badge.setAlignment(Pos.CENTER);
                badge.setContentDisplay(ContentDisplay.CENTER);
                badge.setPadding(new Insets(3, 10, 3, 10));
            }
            
            @Override
            protected void updateItem(Boolean recyclable, boolean empty) {
                super.updateItem(recyclable, empty);
                if (empty || recyclable == null) {
                    setGraphic(null);
                } else {
                    if (recyclable) {
                        badge.setText("ECO FRIENDLY ♻");
                        badge.getStyleClass().setAll("recyclable-badge");
                    } else {
                        badge.setText("NOT ECO");
                        badge.getStyleClass().setAll("non-recyclable-badge");
                    }
                    setGraphic(badge);
                }
            }
        });
        
        imageColumn.setCellFactory(column -> new TableCell<Produit, String>() {
            private ImageView imageView;
            
            {
                imageView = new ImageView();
                imageView.setFitWidth(80);
                imageView.setFitHeight(60);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setCache(true);
                imageView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(46,125,50,0.2), 5, 0, 0, 1);");
            }
            
            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty || imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    String imageFileName = imagePath.trim();
                    java.io.File imgFile = new java.io.File("src/main/resources/images/products/" + imageFileName);
                    if (imgFile.exists() && imgFile.isFile()) {
                        imageView.setImage(null);
                        imageView.setImage(new Image("file:src/main/resources/images/products/" + imageFileName));
                    } else {
                        imageView.setImage(new Image("file:src/main/resources/images/default_renewable.png"));
                    }
                    setGraphic(new StackPane(imageView));
                }
            }
        });
    }

    public void loadProductData() {
        productTable.getItems().setAll(produitService.recuperer());
    }

    private void setupSelectionListener() {
        productTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                updateButton.setDisable(newSelection == null);
                previewButton.setDisable(newSelection == null);
            });
    }

    private void loadCategories() {
        categoryMap.clear();
        categoryCombo.getItems().clear();
        categoryCombo.getItems().add("All Categories");

        try {
            CategorieService categorieService = new CategorieService();
            var categories = categorieService.recuperer();

            for (var category : categories) {
                categoryMap.put(category.getNom(), category.getId());
                categoryCombo.getItems().add(category.getNom());
            }

            categoryCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null || newVal.equals("All Categories")) {
                    productTable.setItems(FXCollections.observableArrayList(produitService.recuperer()));
                } else {
                    int categoryId = categoryMap.get(newVal);
                    productTable.setItems(FXCollections.observableArrayList(
                        produitService.recuperer().stream()
                            .filter(p -> p.getCategorieId() == categoryId)
                            .collect(Collectors.toList())
                    ));
                }
            });

        } catch (Exception e) {
            UIUtils.showAlert("Error", null, "Failed to load categories: " + e.getMessage(), "ERROR");
        }
    }

    @FXML
    private void handleAdd() {
        try {
            Stage stage = (Stage) productTable.getScene().getWindow();
            ProductFormController controller = loadViewAndSwitchScene(stage, "/views/ProductFormView.fxml");
            controller.setCategories(categoryMap);
            controller.setMode(ProductFormController.Mode.ADD);
            controller.setDashboardController(this);
        } catch (Exception e) {
            UIUtils.showAlert("Error", null, "Failed to open form: " + e.getMessage(), "ERROR");
        }
    }

    @FXML
    private void handleUpdate() {
        Produit selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                Stage stage = (Stage) productTable.getScene().getWindow();
                ProductFormController controller = loadViewAndSwitchScene(stage, "/views/ProductFormView.fxml");
                controller.setCategories(categoryMap);
                controller.setMode(ProductFormController.Mode.EDIT);
                controller.setProduct(selected);
                controller.setDashboardController(this);
            } catch (Exception e) {
                UIUtils.showAlert("Error", null, "Failed to open form: " + e.getMessage(), "ERROR");
            }
        }
    }

    public void showDashboardView(Stage stage) {
        try {
            switchScene(stage, "/views/ProductDashboardView.fxml");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductDashboardView.fxml"));
            Parent root = loader.load();
            ProductDashboardController controller = loader.getController();
            controller.setCategoryMap(this.categoryMap);
            controller.loadProductData();
        } catch (Exception e) {
            UIUtils.showAlert("Error", null, "Failed to load dashboard view: " + e.getMessage(), "ERROR");
        }
    }

    public void setCategoryMap(Map<String, Integer> categoryMap) {
        this.categoryMap = categoryMap;
    }

    @FXML
    private void handleDelete() {
        Produit selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Delete product: " + selected.getNom());
            confirm.setContentText("Are you sure?");
            
            if (confirm.showAndWait().get() == ButtonType.OK) {
                produitService.supprimer(selected);
                loadProductData();
            }
        }
    }

    @FXML
    private void handleAddCategory() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Category");
        dialog.setHeaderText("Enter new category name:");
        Optional<String> result = dialog.showAndWait();
        
        result.ifPresent(name -> {
            if (!name.isEmpty()) {
                if (!categoryMap.containsKey(name)) {
                    try {
                        CategorieService categorieService = new CategorieService();
                        Categorie newCategory = new Categorie(0, name, "");
                        categorieService.ajouter(newCategory);
                        
                        categoryMap.put(name, newCategory.getId());
                        categoryCombo.getItems().add(name);
                        categoryCombo.getSelectionModel().select(name);
                    } catch (Exception e) {
                        UIUtils.showAlert("Error", null, "Failed to add category: " + e.getMessage(), "ERROR");
                    }
                } else {
                    UIUtils.showAlert("Warning", null, "Category already exists", "WARNING");
                }
            }
        });
    }

    @FXML
    private void handleDeleteCategory() {
        String selected = categoryCombo.getValue();
        if (selected != null && !selected.equals("All Categories")) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Delete category: " + selected);
            confirm.setContentText("Are you sure?");
            
            if (confirm.showAndWait().get() == ButtonType.OK) {
                try {
                    CategorieService categorieService = new CategorieService();
                    Categorie categoryToDelete = new Categorie(
                        categoryMap.get(selected),
                        selected,
                        ""
                    );
                    categorieService.supprimer(categoryToDelete);
                    loadCategories();
                } catch (Exception e) {
                    UIUtils.showAlert("Error", null, "Failed to delete category: " + e.getMessage(), "ERROR");
                }
            }
        } else {
            UIUtils.showAlert("Warning", null, "Please select a valid category to delete.", "WARNING");
        }
    }
    @FXML
    private void handlePreview() {
        Produit selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ProductPreviewView.fxml"));
                Parent root = loader.load();
                
                ProductPreviewController controller = loader.getController();
                controller.setProduct(selected);
                
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Product Preview");
                stage.showAndWait();

                loadProductData();
            } catch (Exception e) {
            UIUtils.showAlert("Error", null, "Failed to open preview: " + e.getMessage(), "ERROR");
            }
        } else {
            UIUtils.showAlert("Warning", null, "Please select a product to preview.", "WARNING");
        }
    }

}
