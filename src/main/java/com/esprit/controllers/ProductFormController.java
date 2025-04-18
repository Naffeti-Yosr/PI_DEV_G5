package com.esprit.controllers;

import com.esprit.models.Produit;
import com.esprit.Services.ProduitService;
import com.esprit.utils.ImageUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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

  

    public javafx.collections.ObservableList<Produit> loadData() {
        return javafx.collections.FXCollections.observableArrayList();
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
                // Check if image file exists, else load default image
                java.io.File imgFile = new java.io.File("src/main/resources/images/products/" + produit.getImage());
                if (imgFile.exists() && imgFile.isFile()) {
                    productImage.setImage(ImageUtils.loadImage(produit.getImage()));
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

    public boolean isSaved() {
        return saved;
    }

    @FXML
    private void handleSave() {
            produit.setNom(nameField.getText());
            produit.setDescription(descriptionField.getText());
            produit.setPrix(Double.parseDouble(priceField.getText()));
            produit.setQuantiteStock(Integer.parseInt(stockField.getText()));
            produit.setRecyclable(recyclableCheck.isSelected());
            
            String selectedCategory = categoryCombo.getValue();
            if (selectedCategory != null && !selectedCategory.isEmpty()) {
                produit.setCategorieId(categoryMap.get(selectedCategory));
            }

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
            
            Stage stage = (Stage) nameField.getScene().getWindow();
            if (stage.getOwner() != null) {
                ProductDashboardController dashboard = 
                    (ProductDashboardController) stage.getOwner().getUserData();
                dashboard.refreshProduct(produit);
            }
            
            closeForm();
    }

    @FXML
    private void handleCancel() {
        closeForm();
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
        imageFile = null;
        productImage.setImage(ImageUtils.loadImage("default_renewable.png"));
        if (produit != null) {
            produit.setImage("default_renewable.png");
            ProduitService produitService = new ProduitService();
            produitService.modifier(produit);
            // Refresh product image in dashboard after deletion
            Stage stage = (Stage) nameField.getScene().getWindow();
            if (stage.getOwner() != null) {
                ProductDashboardController dashboard = 
                    (ProductDashboardController) stage.getOwner().getUserData();
                if (dashboard != null) {
                    dashboard.refreshProduct(produit);
                }
            }
        }
    }

    private void closeForm() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
