package com.esprit.models;

public class Produit {
    private int id;
    private String nom;
    private String description;
    private String picture; // Path or URL to the product image
    private float price;    // Price of the product
    private int categorieId; // Foreign key to Categorie

    public Produit() {
    }

    public Produit(int id, String nom, String description, String picture, float price, int categorieId) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.picture = picture;
        this.price = price;
        this.categorieId = categorieId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(int categorieId) {
        this.categorieId = categorieId;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", picture='" + picture + '\'' +
                ", price=" + price +
                ", categorieId=" + categorieId +
                '}';
    }
}