package com.esprit.models;

public class Commande {
    private int id;
    private int utilisateurId;
    private float total;

    public Commande() {
    }

    public Commande(int id, int utilisateurId, float total) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.total = total;
    }

    public Commande(int id, float total, int utilisateurId) {
        this.id = id;
        this.total = total;
        this.utilisateurId = utilisateurId;
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

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", utilisateurId=" + utilisateurId +
                ", total=" + total +
                '}';
    }
}