package com.esprit.models;

public class PanierProduit {
    private int id;
    private Produit produit;
    private int quantite;
    private double sousTotal;

    public PanierProduit(int id, Produit produit, int quantite) {
        this.id = id;
        this.produit = produit;
        this.quantite = quantite;
        calculateSousTotal();
    }

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
        calculateSousTotal();
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
        calculateSousTotal();
    }

    public double getSousTotal() {
        return sousTotal;
    }

    private void calculateSousTotal() {
        if (produit != null) {
            this.sousTotal = this.quantite * produit.getPrix();
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