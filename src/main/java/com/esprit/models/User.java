package com.esprit.models;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String nom; // Last Name
    private String prenom; // First Name
    private LocalDateTime birthDate; // Birth Date
    private String email; // Email Address
    private String password; // Password
    private String role; // Role (e.g., admin, client, employee)

    // Constructor
    public User(int id, String nom, String prenom, LocalDateTime birthDate, String email, String password, String role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", birthDate=" + birthDate +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}