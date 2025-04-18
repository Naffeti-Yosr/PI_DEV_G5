package com.esprit.models;

import java.time.LocalDateTime;

public class Poster {
    private int id;
    private String titre;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
    private int evenementId;
    private int createdById;

    public Poster() {
    }

    public Poster(int id, String titre, String description, String imageUrl, LocalDateTime createdAt, int evenementId, int createdById) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.evenementId = evenementId;
        this.createdById = createdById;
    }
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(int evenementId) {
        this.evenementId = evenementId;
    }

    public int getCreatedById() {
        return createdById;
    }

    public void setCreatedById(int createdById) {
        this.createdById = createdById;
    }

    public Poster(int id, String imagePath) {
        this.id = id;
        this.imageUrl = imagePath;
    }

    public String getImagePath() {
        return imageUrl;
    }
}