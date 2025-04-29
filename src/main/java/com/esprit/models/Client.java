package com.esprit.models;

import java.util.Date;

public class Client extends User {
    private String address;
    private String phoneNumber;
    private String companyName;
    private String companyAddress;
    private String companyPhoneNumber;

    public Client(String address, String phoneNumber, String companyName, String companyAddress, String companyPhoneNumber) {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyPhoneNumber = companyPhoneNumber;
    }

    public Client(String nom, String prenom, Date birth_date, String email, String address, String phoneNumber, String password, String address1, String phoneNumber1, String companyName, String companyAddress, String companyPhoneNumber) {
        super(nom, prenom, birth_date, email, address, phoneNumber, password);
        this.address = address1;
        this.phoneNumber = phoneNumber1;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyPhoneNumber = companyPhoneNumber;
    }

    public Client(int id, String nom, String prenom, Date birth_date, String email, String address, String phoneNumber, String password, UserRole role, UserStatus status, String address1, String phoneNumber1, String companyName, String companyAddress, String companyPhoneNumber) {
        super(id, nom, prenom, birth_date, email, address, phoneNumber, password, role, status);
        this.address = address1;
        this.phoneNumber = phoneNumber1;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyPhoneNumber = companyPhoneNumber;
    }

    public Client(int id, String nom, String prenom, Date birthDate, String email, String address, String phoneNumber, String password, UserRole role, String address1, String phoneNumber1, String companyName, String companyAddress, String companyPhoneNumber) {
        super(id, nom, prenom, birthDate, email, address, phoneNumber, password, role);
        this.address = address1;
        this.phoneNumber = phoneNumber1;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyPhoneNumber = companyPhoneNumber;
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

