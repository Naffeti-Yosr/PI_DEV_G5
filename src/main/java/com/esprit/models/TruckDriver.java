package com.esprit.models;

import java.util.Date;

public class TruckDriver extends User{
    private String DriverLicense;
    private String truckType;

    public TruckDriver(int id, String nom, String prenom, Date birth_date, String email, String password, String role, String driverLicense, String truckType) {
        super(id, nom, prenom, birth_date, email, password, role);
        DriverLicense = driverLicense;
        this.truckType = truckType;
    }

    public TruckDriver(String driverLicense, String truckType) {
        DriverLicense = driverLicense;
        this.truckType = truckType;
    }

    @Override
    public String toString() {
        return "TruckDriver{" +
                "DriverLicense='" + DriverLicense + '\'' +
                ", truckType='" + truckType + '\'' +
                '}';
    }
}
