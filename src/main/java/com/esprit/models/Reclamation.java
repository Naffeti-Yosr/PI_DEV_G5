package com.esprit.models;

import java.util.Date;

public class Reclamation {
    private int id;
    private int userId;
    private String nom;
    private String prenom;
    private String report;
    private String status;
    private Date createdAt;

    public Reclamation() {
        this.status = "Pending";
        this.createdAt = new Date();
    }

    public Reclamation(int id, int userId, String report, String status, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.report = report;
        this.status = status != null ? status : "Pending";
        this.createdAt = createdAt != null ? createdAt : new Date();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status != null ? status : "Pending";
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt != null ? createdAt : new Date();
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", userId=" + userId +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", report='" + report + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
