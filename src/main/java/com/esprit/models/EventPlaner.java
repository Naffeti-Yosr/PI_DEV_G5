package com.esprit.models;

import java.util.Date;

public class EventPlaner extends User{
    private String fieldOfExpertise;

    public EventPlaner(String fieldOfExpertise) {
        this.fieldOfExpertise = fieldOfExpertise;
    }

    public EventPlaner(String nom, String prenom, Date birth_date, String email, String address, String phoneNumber, String password, String fieldOfExpertise) {
        super(nom, prenom, birth_date, email, address, phoneNumber, password);
        this.fieldOfExpertise = fieldOfExpertise;
    }

    public EventPlaner(int id, String nom, String prenom, Date birth_date, String email, String address, String phoneNumber, String password, UserRole role, UserStatus status, String fieldOfExpertise) {
        super(id, nom, prenom, birth_date, email, address, phoneNumber, password, role, status);
        this.fieldOfExpertise = fieldOfExpertise;
    }

    public EventPlaner(int id, String nom, String prenom, Date birthDate, String email, String address, String phoneNumber, String password, UserRole role, String fieldOfExpertise) {
        super(id, nom, prenom, birthDate, email, address, phoneNumber, password, role);
        this.fieldOfExpertise = fieldOfExpertise;
    }

    public EventPlaner() {
        super();
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
