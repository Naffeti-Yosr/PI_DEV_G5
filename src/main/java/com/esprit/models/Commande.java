package com.esprit.models;

import java.util.ArrayList;
import java.util.List;

public class Commande {
    private int id;
    private int utilisateurId;
    private float total;
    private List<CommandeProduit> commandeProduits; // Many-to-many relationship with Produit

    public Commande() {
        commandeProduits = new ArrayList<>();
    }

    public Commande(int id, int utilisateurId, float total) {
        this.id = id;
        this.utilisateurId = utilisateurId;
        this.total = total;
        commandeProduits = new ArrayList<>();
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

    public List<CommandeProduit> getCommandeProduits() {
        return commandeProduits;
    }

    public void setCommandeProduits(List<CommandeProduit> commandeProduits) {
        this.commandeProduits = commandeProduits;
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