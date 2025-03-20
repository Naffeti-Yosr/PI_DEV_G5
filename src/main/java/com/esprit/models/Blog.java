package com.esprit.models;

import java.util.Date;

public class Blog {
    private int id;
    private String titre;
    private String contenu;
    private int auteurId;
    private Date datePublication;

    public Blog(int id, String titre, String contenu, int auteurId, Date datePublication) {
        this.id = id;
        this.titre = titre;
        this.contenu = contenu;
        this.auteurId = auteurId;
        this.datePublication = datePublication;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public int getAuteurId() { return auteurId; }
    public void setAuteurId(int auteurId) { this.auteurId = auteurId; }

    public Date getDatePublication() { return datePublication; }
    public void setDatePublication(Date datePublication) { this.datePublication = datePublication;}
}