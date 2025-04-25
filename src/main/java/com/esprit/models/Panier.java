package com.esprit.models;

import java.util.List;

public class Panier {
    private int id;
    private int utilisateurId;
    private double total;
    private List<PanierProduit> produits;

    public Panier(int id, int utilisateurId, double total, List<PanierProduit> produits) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.total = total;
        this.produits = produits;
    }

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
        calculateTotal();
    }

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