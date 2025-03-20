package com.esprit.models;

public class CommandeProduit {
    private int commandeId;
    private int produitId;

    public CommandeProduit() {
    }

    public CommandeProduit(int commandeId, int produitId) {
        this.commandeId = commandeId;
        this.produitId = produitId;
    }

    // Getters and Setters
    public int getCommandeId() {
        return commandeId;
    }

    public void setCommandeId(int commandeId) {
        this.commandeId = commandeId;
    }

    public int getProduitId() {
        return produitId;
    }

    public void setProduitId(int produitId) {
        this.produitId = produitId;
    }

    @Override
    public String toString() {
        return "CommandeProduit{" +
                "commandeId=" + commandeId +
                ", produitId=" + produitId +
                '}';
    }
}
