package com.esprit.models;

import java.util.Date;

public class TruckDriver extends User{
    private String DriverLicense;
    private String truckType;

    public TruckDriver(String driverLicense, String truckType) {
        DriverLicense = driverLicense;
        this.truckType = truckType;
    }

    public TruckDriver(String nom, String prenom, Date birth_date, String email, String address, String phoneNumber, String password, String driverLicense, String truckType) {
        super(nom, prenom, birth_date, email, address, phoneNumber, password);
        DriverLicense = driverLicense;
        this.truckType = truckType;
    }

    public TruckDriver(int id, String nom, String prenom, Date birth_date, String email, String address, String phoneNumber, String password, UserRole role, UserStatus status, String driverLicense, String truckType) {
        super(id, nom, prenom, birth_date, email, address, phoneNumber, password, role, status);
        DriverLicense = driverLicense;
        this.truckType = truckType;
    }

    public TruckDriver(int id, String nom, String prenom, Date birthDate, String email, String address, String phoneNumber, String password, UserRole role, String driverLicense, String truckType) {
        super(id, nom, prenom, birthDate, email, address, phoneNumber, password, role);
        DriverLicense = driverLicense;
        this.truckType = truckType;
    }

    public TruckDriver() {
        super();
    }

    @Override
    public String toString() {
        return "TruckDriver{" +
                "DriverLicense='" + DriverLicense + '\'' +
                ", truckType='" + truckType + '\'' +
                '}';
    }
}
