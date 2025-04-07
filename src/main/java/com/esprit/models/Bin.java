package com.esprit.models;


public class Bin {

    private int id;
    private String location;
    private String typeDechet;
    private double niveauRemplissage;
    private String statut;

    public Bin(int id, String location, String typeDechet, double niveauRemplissage, String statut) {
        this.id = id;
        this.location = location;
        this.typeDechet = typeDechet;
        this.niveauRemplissage = niveauRemplissage;
        this.statut = statut;

    }

    public Bin(String location, String typeDechet, double niveauRemplissage, String statut) {
        this.location = location;
        this.typeDechet = typeDechet;
        this.niveauRemplissage = niveauRemplissage;
        this.statut = statut;

    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public double getNiveauRemplissage() {
        return niveauRemplissage;
    }

    public String getStatut() {
        return statut;
    }

    public String getTypeDechet() {
        return typeDechet;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNiveauRemplissage(double niveauRemplissage) {
        this.niveauRemplissage = niveauRemplissage;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setTypeDechet(String typeDechet) {
        this.typeDechet = typeDechet;
    }

    @Override
    public String toString() {
        return "Bin{" +
                "id=" + id +
                ", location='" + location + '\'' +
                ", typeDechet='" + typeDechet + '\'' +
                ", niveauRemplissage=" + niveauRemplissage +
                ", statut='" + statut + '\'' +
                '}';
    }
}
