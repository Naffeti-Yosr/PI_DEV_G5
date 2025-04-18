package com.esprit.models;

public class PanierProduit {
    private int id;
    private Produit produit; // Associated product
    private int quantite; // Quantity of the product
    private double sousTotal; // Subtotal for this product in the cart

    // Constructor
    public PanierProduit(int id, Produit produit, int quantite) {
        this.id = id;
        this.produit = produit;
        this.quantite = quantite;
        calculateSousTotal(); // Automatically calculate subtotal on creation
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
        calculateSousTotal(); // Recalculate subtotal when product changes
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
        calculateSousTotal(); // Recalculate subtotal when quantity changes
    }

    public double getSousTotal() {
        return sousTotal;
    }

    // Calculate the subtotal (quantite * produit.prix)
    private void calculateSousTotal() {
        if (produit != null) {
            this.sousTotal = quantite * produit.getPrix();
        }
    }

    @Override
    public String toString() {
        return "PanierProduit{" +
                "id=" + id +
                ", produit=" + produit +
                ", quantite=" + quantite +
                ", sousTotal=" + sousTotal +
                '}';
    }
}