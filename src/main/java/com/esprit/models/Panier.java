package com.esprit.models;

import java.util.List;

public class Panier {
    private int id;
    private int utilisateurId; // ID of the user owning the cart
    private double total; // Total sum of all products in the cart
    private List<PanierProduit> produits; // List of products in the cart

    // Constructor
    public Panier(int id, int utilisateurId, double total, List<PanierProduit> produits) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.total = total;
        this.produits = produits;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(int utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<PanierProduit> getProduits() {
        return produits;
    }

    public void setProduits(List<PanierProduit> produits) {
        this.produits = produits;
        calculateTotal(); // Automatically update total when products are set
    }

    // Calculate the total sum of the cart
    public void calculateTotal() {
        this.total = produits.stream().mapToDouble(PanierProduit::getSousTotal).sum();
    }

    @Override
    public String toString() {
        return "Panier{" +
                "id=" + id +
                ", utilisateurId=" + utilisateurId +
                ", total=" + total +
                ", produits=" + produits +
                '}';
    }
}