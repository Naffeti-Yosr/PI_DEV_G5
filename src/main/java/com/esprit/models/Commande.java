package com.esprit.models;

import java.util.Date;

public class Commande {
    private int id;
    private Date date;
    private String statut;
    private int clientId;

    public Commande() {
    }

    public Commande(int id, Date date, String statut, int clientId) {
        this.id = id;
        this.date = date;
        this.statut = statut;
        this.clientId = clientId;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
