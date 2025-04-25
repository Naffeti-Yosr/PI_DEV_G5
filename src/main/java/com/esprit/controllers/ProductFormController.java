package com.esprit.controllers;

import com.esprit.models.Produit;
import com.esprit.Services.ProduitService;
import com.esprit.utils.ImageUtils;
import com.esprit.utils.UIUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ProductFormController {
    public enum Mode {
        ADD, EDIT
    }

    @FXML private TextField nameField;
    @FXML private TextField descriptionField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private CheckBox recyclableCheck;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private ImageView productImage;
    
    private Produit produit;
    private boolean saved = false;
    private Mode mode;
    private Map<String, Integer> categoryMap = new HashMap<>();
    private File imageFile;

    private ProductDashboardController dashboardController;

    public void setDashboardController(ProductDashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }


    public void setProduct(Produit produit) {
        this.produit = produit;
        if (mode == Mode.EDIT) {
            nameField.setText(produit.getNom());
            descriptionField.setText(produit.getDescription());
            priceField.setText(String.format("%.2f", produit.getPrix()));
            stockField.setText(String.valueOf(produit.getQuantiteStock()));
            recyclableCheck.setSelected(produit.isRecyclable());
            
            categoryMap.forEach((name, id) -> {
                if (id == produit.getCategorieId()) {
                    categoryCombo.getSelectionModel().select(name);
                }
            });

            if (produit.getImage() != null && !produit.getImage().isEmpty()) {
                String imageFileName = produit.getImage().trim();
                java.io.File imgFile = new java.io.File("src/main/resources/images/products/" + imageFileName);
                if (imgFile.exists() && imgFile.isFile()) {
                    productImage.setImage(ImageUtils.loadImage(imageFileName));
                } else {
                    productImage.setImage(ImageUtils.loadImage("default_renewable.png"));
                }
            }
        }
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.ADD) {
            this.produit = new Produit(0, "", "", 0.0, 0, "", false, 0);
        }
    }

    public void setCategories(Map<String, Integer> categories) {
        this.categoryMap = categories;
        categoryCombo.getItems().clear();
        categoryCombo.getItems().addAll(categories.keySet());
    }



    @FXML
    private void handleSave() {
        // Input validation
        String name = nameField.getText();
        String description = descriptionField.getText();
        String priceText = priceField.getText();
        String stockText = stockField.getText();
        String selectedCategory = categoryCombo.getValue();

        if (name == null || name.trim().isEmpty()) {
            UIUtils.showAlert("Validation Error", null, "Name is required.", "ERROR");
            return;
        }
        if (description == null || description.trim().isEmpty()) {
            UIUtils.showAlert("Validation Error", null, "Description is required.", "ERROR");
            return;
        }
        if (priceText == null || priceText.trim().isEmpty()) {
            UIUtils.showAlert("Validation Error", null, "Price is required.", "ERROR");
            return;
        }
        double price;
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) {
                UIUtils.showAlert("Validation Error", null, "Price cannot be negative.", "ERROR");
                return;
            }
        } catch (NumberFormatException e) {
            UIUtils.showAlert("Validation Error", null, "Price must be a valid number.", "ERROR");
            return;
        }
        if (stockText == null || stockText.trim().isEmpty()) {
            UIUtils.showAlert("Validation Error", null, "Stock quantity is required.", "ERROR");
            return;
        }
        int stock;
        try {
            stock = Integer.parseInt(stockText);
            if (stock < 0) {
                UIUtils.showAlert("Validation Error", null, "Stock quantity cannot be negative.", "ERROR");
                return;
            }
        } catch (NumberFormatException e) {
            UIUtils.showAlert("Validation Error", null, "Stock quantity must be a valid integer.", "ERROR");
            return;
        }
        if (selectedCategory == null || selectedCategory.trim().isEmpty()) {
            UIUtils.showAlert("Validation Error", null, "Category must be selected.", "ERROR");
            return;
        }
        if (!categoryMap.containsKey(selectedCategory)) {
            UIUtils.showAlert("Validation Error", null, "Selected category is invalid.", "ERROR");
            return;
        }

        // Set product fields after validation
        produit.setNom(name);
        produit.setDescription(description);
        produit.setPrix(price);
        produit.setQuantiteStock(stock);
        produit.setRecyclable(recyclableCheck.isSelected());
        produit.setCategorieId(categoryMap.get(selectedCategory));

        ProduitService produitService = new ProduitService();

        if (imageFile != null) {
            if (produit.getImage() != null && !produit.getImage().isEmpty() && !produit.getImage().equals("default_renewable.png")) {
                ImageUtils.deleteProductImage(produit.getImage());
            }
            String imagePath = ImageUtils.saveProductImage(imageFile, produit.getNom());
            produit.setImage(imagePath);
        }

        if (mode == Mode.ADD) {
            produitService.ajouter(produit);
        } else {
            produitService.modifier(produit);
        }

        saved = true;

        if (dashboardController != null) {
            dashboardController.loadProductData();
        }

        backToDashboard();
    }

    @FXML
    private void handleBack() {
        backToDashboard();
    }

    private void backToDashboard() {
        try {
            if (dashboardController != null) {
                javafx.stage.Stage stage = (javafx.stage.Stage) productImage.getScene().getWindow();
                dashboardController.showDashboardView(stage);
            }
        } catch (Exception e) {
            UIUtils.showAlert("Navigation Error", null, "Failed to return to dashboard: " + e.getMessage(), "ERROR");
        }
    }

    @FXML
    private void handleBrowseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        imageFile = fileChooser.showOpenDialog(productImage.getScene().getWindow());
        
        if (imageFile != null) {
            productImage.setImage(new Image(imageFile.toURI().toString()));
        }
    }

    @FXML
    private void handleDeleteImage() {
        if (produit != null && produit.getImage() != null && !produit.getImage().isEmpty() && !"default_renewable.png".equals(produit.getImage())) {
            ImageUtils.deleteProductImage(produit.getImage());
        }
        imageFile = null;
        productImage.setImage(ImageUtils.loadImage("default_renewable.png"));
        if (produit != null) {
            produit.setImage("default_renewable.png");
            ProduitService produitService = new ProduitService();
            produitService.modifier(produit);
            if (dashboardController != null) {
                dashboardController.loadProductData();
            }
        }
    }
}
