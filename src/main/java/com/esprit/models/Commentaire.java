package com.esprit.models;

import java.util.Date;

public class Commentaire {
    private int id;
    private String content;
    private Date date;
    private User auteur;
    private Blog blog; // Reference to the blog this comment belongs to


    public Commentaire(int id, String content, Date date, User auteur, Blog blog) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.auteur = auteur;
        this.blog = blog;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }
}