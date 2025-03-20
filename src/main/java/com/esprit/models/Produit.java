package com.esprit.models;

public class Produit {
    private int id;
    private String nom;
    private String description;
    private int categorieId;

    public Produit() {
    }

    public Produit(int id, String nom, String description, int categorieId) {
        this.id = id;
        this.nom = nom;
        this.description = description;
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
                ", categorieId=" + categorieId +
                '}';
    }
}