package com.esprit.models;

import java.time.LocalDateTime;

public class Blog {
    private int id;
    private String titre;
    private String resume;
    private String contenu;
    private String image;
    private LocalDateTime datePublication;
    private int auteurId;
    private int views;
    private String status;

    // Constructors
    public Blog() {}
    
    public Blog(int id, String titre, String resume, String contenu, String image, 
                LocalDateTime datePublication, int auteurId, int views, String status) {
        this.id = id;
        this.titre = titre;
        this.resume = resume;
        this.contenu = contenu;
        this.image = image;
        this.datePublication = datePublication;
        this.auteurId = auteurId;
        this.views = views;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }
    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }
    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    public LocalDateTime getDatePublication() { return datePublication; }
    public void setDatePublication(LocalDateTime datePublication) { this.datePublication = datePublication; }
    public int getAuteurId() { return auteurId; }
    public void setAuteurId(int auteurId) { this.auteurId = auteurId; }
    public int getViews() { return views; }
    public void setViews(int views) { this.views = views; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}