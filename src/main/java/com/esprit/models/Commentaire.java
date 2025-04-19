package com.esprit.models;

import java.time.LocalDateTime;

public class Commentaire {
    private int id;
    private String contenu;
    private LocalDateTime dateCommentaire;
    private int auteurId;
    private int blogId;

    // Constructors
    public Commentaire() {}
    
    public Commentaire(int id, String contenu, LocalDateTime dateCommentaire, int auteurId, int blogId) {
        this.id = id;
        this.contenu = contenu;
        this.dateCommentaire = dateCommentaire;
        this.auteurId = auteurId;
        this.blogId = blogId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public LocalDateTime getDateCommentaire() { return dateCommentaire; }
    public void setDateCommentaire(LocalDateTime dateCommentaire) { this.dateCommentaire = dateCommentaire; }
    public int getAuteurId() { return auteurId; }
    public void setAuteurId(int auteurId) { this.auteurId = auteurId; }
    public int getBlogId() { return blogId; }
    public void setBlogId(int blogId) { this.blogId = blogId; }
}