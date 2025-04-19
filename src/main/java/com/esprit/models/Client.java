package com.esprit.models;

import java.util.Date;

public class Client extends User {
    private String address;
    private String phoneNumber;
    private String companyName;
    private String companyAddress;
    private String companyPhoneNumber;

    public Client(int id, String nom, String prenom, Date birth_date, String email, String password, String role, String address, String phoneNumber) {
        super(id, nom, prenom, birth_date, email, password, role);
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
    

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Client{" +
                "address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}

