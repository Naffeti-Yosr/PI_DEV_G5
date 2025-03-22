package com.esprit.models;

import lombok.*;

import java.util.Date;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private Date birth_date;
    private String email;
    private String password;
    private String role;

   public User(int id, String nom, String prenom, Date birth_date, String email, String password, String role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.birth_date = birth_date;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String nom, String prenom, Date birth_date, String email, String password, String role) {
        this.nom = nom;
        this.prenom = prenom;
        this.birth_date = birth_date;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getNom() {return nom;}

    public void setNom(String nom) {this.nom = nom;}

    public String getPrenom() {return prenom;}

    public void setPrenom(String prenom) {this.prenom = prenom;}

    public Date getBirth_date() {return birth_date;}

    public void setBirth_date(Date birth_date) {this.birth_date = birth_date;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getRole() {return role;}

    public void setRole(String role) {this.role = role;}

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", birth_date=" + birth_date +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}



