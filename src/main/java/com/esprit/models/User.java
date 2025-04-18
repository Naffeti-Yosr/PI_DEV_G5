package com.esprit.models;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private java.time.LocalDateTime birthDate;
    private String email;
    private String password;
    private String role;

    public User() {}

    public User(int id, String nom, String prenom, java.time.LocalDateTime birthDate, String email, String password, String role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public java.time.LocalDateTime getBirthDate() { return birthDate; }
    public void setBirthDate(java.time.LocalDateTime birthDate) { this.birthDate = birthDate; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

