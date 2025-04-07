package com.esprit.models;

public class Truck {
    private int id;
    private double capaciteMax;
    private double niveauRemplissageActuel;
    private String section;
    private String statut;

    public Truck(double capaciteMax, double niveauRemplissageActuel, String section, String statut) {
        this.capaciteMax = capaciteMax;
        this.niveauRemplissageActuel = niveauRemplissageActuel;
        this.section = section;
        this.statut = statut;
    }


    public Truck(int id, double capaciteMax, double niveauRemplissageActuel, String section, String statut) {
        this.id = id;
        this.capaciteMax = capaciteMax;
        this.niveauRemplissageActuel = niveauRemplissageActuel;
        this.section = section;
        this.statut = statut;
    }


    public double getCapaciteMax() {
        return capaciteMax;
    }

    public int getId() {
        return id;
    }

    public double getNiveauRemplissageActuel() {
        return niveauRemplissageActuel;
    }

    public String getSection() {
        return section;
    }

    public String getStatut() {
        return statut;
    }

    public void setCapaciteMax(double capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNiveauRemplissageActuel(double niveauRemplissageActuel) {
        this.niveauRemplissageActuel = niveauRemplissageActuel;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return "Truck{" +
                "capaciteMax=" + capaciteMax +
                ", id=" + id +
                ", niveauRemplissageActuel=" + niveauRemplissageActuel +
                ", section='" + section + '\'' +
                ", statut='" + statut + '\'' +
                '}';
    }
}
