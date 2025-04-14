package com.esprit.models;

import java.time.LocalDateTime;
import java.util.List;

public class Evenement {
    private int id;
    private String titre;
    private String description;
    private LocalDateTime date;
    private String adresse;
    private User organisateur;
    private List<User> participants;



    public Evenement() {
    }

    public Evenement(int id, String titre, LocalDateTime date, String description, String adresse, User organisateur, List<User> participants) {
        this.id = id;
        this.titre = titre;
        this.date = date;
        this.description = description;
        this.adresse = adresse;
        this.organisateur = organisateur;
        this.participants = participants;
    }

    public Evenement(int id, String titre, String description, LocalDateTime date, String adresse, User organisateur) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.adresse = adresse;
        this.organisateur = organisateur;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public User getOrganisateur() {
        return organisateur;
    }

    public void setOrganisateur(User organisateur) {
        this.organisateur = organisateur;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }
}