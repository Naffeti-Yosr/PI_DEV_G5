package com.esprit.models;

import java.util.Date;

public class User {
    private int id;
    private String nom;
    private String prenom;
    private Date birth_date;
    private String email;
    private String address;
    private String phoneNumber;
    private String password;
    private UserRole role;
    private UserStatus status;

    public User() {
        this.status = UserStatus.NON_CONFIRMED; // Default status for new users
    }

    // Constructor without role (for user registration)
    public User(String nom, String prenom, Date birth_date, String email, String address, String phoneNumber, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.birth_date = birth_date;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.status = UserStatus.NON_CONFIRMED;
    }

    // Full constructor (for admin use)
    public User(int id, String nom, String prenom, Date birth_date, String email, String address, String phoneNumber, String password, UserRole role, UserStatus status) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.birth_date = birth_date;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.status = status != null ? status : UserStatus.NON_CONFIRMED;
    }

    // Constructor with role but without status (will use default NON_CONFIRMED status)
    public User(int id, String nom, String prenom, Date birthDate, String email, String address, String phoneNumber, String password, UserRole role) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.birth_date = birthDate;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        this.status = UserStatus.NON_CONFIRMED;
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

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public String getPhoneNumber() {return phoneNumber;}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}


    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {return status;}

    public void setStatus(UserStatus status) {
        this.status = status != null ? status : UserStatus.NON_CONFIRMED;
    }

    public void setStatusFromString(String statusStr) {
        try {
            this.status = UserStatus.fromString(statusStr);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid status value: " + statusStr + ". Setting to NON_CONFIRMED.");
            this.status = UserStatus.NON_CONFIRMED;
        }
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, nom='%s', prenom='%s', email='%s', role='%s', status='%s'}",
                id, nom, prenom, email, role != null ? role.getValue() : "null", status != null ? status.getValue() : "null");
    }
}
