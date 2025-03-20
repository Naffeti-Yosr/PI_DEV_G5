package com.esprit.models;

import java.util.Date;

public class Commentaire {
    private int id;
    private String contenu;
    private int auteurId;
    private int blogId;
    private Date date;

    public Commentaire(int id, String contenu, int auteurId, int blogId, Date date) {
        this.id = id;
        this.contenu = contenu;
        this.auteurId = auteurId;
        this.blogId = blogId;
        this.date = date;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public int getAuteurId() { return auteurId; }
    public void setAuteurId(int auteurId) { this.auteurId = auteurId; }

    public int getBlogId() { return blogId; }
    public void setBlogId(int blogId) { this.blogId = blogId; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date=date;}
}