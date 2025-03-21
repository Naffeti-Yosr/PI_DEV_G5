package com.esprit.models;

import java.util.Date;
import java.util.List;

public class Blog {
    private int id;
    private String titre;
    private String description;
    private String imageUrl;
    private Date date;
    private User auteur;
    private List<Commentaire> commentaires; // List of comments for this blog


    public Blog(int id, String titre, String description, String imageUrl, Date date, User auteur) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.imageUrl = imageUrl;
        this.date = date;
        this.auteur = auteur;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getAuteur() {
        return auteur;
    }

    public void setAuteur(User auteur) {
        this.auteur = auteur;
    }

    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }
}