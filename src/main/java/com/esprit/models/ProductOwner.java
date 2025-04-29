package com.esprit.models;

import java.util.Date;

public class ProductOwner extends User{
    public ProductOwner() {
        super();
    }

    public ProductOwner(String nom, String prenom, Date birth_date, String email, String address, String phoneNumber, String password) {
        super(nom, prenom, birth_date, email, address, phoneNumber, password);
    }

    public ProductOwner(int id, String nom, String prenom, Date birth_date, String email, String address, String phoneNumber, String password, UserRole role, UserStatus status) {
        super(id, nom, prenom, birth_date, email, address, phoneNumber, password, role, status);
    }

    public ProductOwner(int id, String nom, String prenom, Date birthDate, String email, String address, String phoneNumber, String password, UserRole role) {
        super(id, nom, prenom, birthDate, email, address, phoneNumber, password, role);
    }
}
