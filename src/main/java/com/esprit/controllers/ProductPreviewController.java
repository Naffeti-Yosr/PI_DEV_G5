package com.esprit.controllers;

import com.esprit.models.Produit;
import com.esprit.Services.ProduitService;
import com.esprit.utils.ImageUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ProductPreviewController {

    @FXML
    private ImageView productImage;

    @FXML
    private Label productName;

    @FXML
    private Label productCategory;

    @FXML
    private Label productPrice;

    @FXML
    private Label productStock;

    @FXML
    private Label productEcoStatus;

    @FXML
    private Label productDescription;

    private Produit produit;

    private final ProduitService produitService = new ProduitService();

    public void setProduct(Produit produit) {
        this.produit = produit;
        if (produit != null) {
            productName.setText(produit.getNom());
            productDescription.setText(produit.getDescription());
            productPrice.setText(String.format("%.2f DT", produit.getPrix()));
            productStock.setText("Stock: " + produit.getQuantiteStock());
            productEcoStatus.setText(produit.isRecyclable() ? "Eco Friendly ♻" : "Not Eco Friendly");
            productImage.setImage(ImageUtils.loadImage(produit.getImage()));

            String categoryName = produitService.getCategoryNameById(produit.getCategorieId());
            productCategory.setText("Category: " + categoryName);
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) productName.getScene().getWindow();
        stage.close();
    }
}
