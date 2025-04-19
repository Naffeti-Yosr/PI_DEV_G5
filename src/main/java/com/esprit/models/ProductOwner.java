package com.esprit.models;

import java.util.Date;

public class ProductOwner extends User{
    public ProductOwner(int id, String nom, String prenom, Date birth_date, String email, String password, String role) {
        super(id, nom, prenom, birth_date, email, password, role);
    }

}
