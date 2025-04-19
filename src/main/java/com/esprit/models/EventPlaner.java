package com.esprit.models;

import java.util.Date;

public class EventPlaner extends User{
    private String fieldOfExpertise;
    public EventPlaner(int id, String nom, String prenom, Date birth_date, String email, String password, String role) {
        super(id, nom, prenom, birth_date, email, password, role);
        this.fieldOfExpertise = fieldOfExpertise;
    }

    public EventPlaner() {
    }

    public String getFieldOfExpertise() {
        return fieldOfExpertise;
    }

    public void setFieldOfExpertise(String fieldOfExpertise) {
        this.fieldOfExpertise = fieldOfExpertise;
    }

    @Override
    public String toString() {
        return "EventPlaner{" +
                "fieldOfExpertise='" + fieldOfExpertise + '\'' +
                '}';
    }
}
