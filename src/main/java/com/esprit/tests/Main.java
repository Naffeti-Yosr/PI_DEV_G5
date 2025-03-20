package com.esprit.tests;

import com.esprit.models.Bin;
import com.esprit.models.Truck;
import com.esprit.services.BinService;
import com.esprit.services.TruckService;

public class Main {
    public static void main(String[] args) {

        BinService binservice = new BinService();
        TruckService truckService = new TruckService();

        Bin bin1 =  new Bin(1, "location1" ,"string" ,25, "statut1");
        binservice.add(bin1);
        binservice.delete(bin1);
        binservice.update(new Bin(2,"location2", "string", 240.0, "statut2"));
        System.out.println(binservice.get());

        //Truck truck1 = new Truck(1,200.0, 0.0, "Quartier A", "au dépôt");
        //truckService.add(truck1);





    }
}
