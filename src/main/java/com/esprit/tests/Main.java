package com.esprit.tests;

import com.esprit.models.Bin;
import com.esprit.models.Truck;
import com.esprit.services.BinService;
import com.esprit.services.TruckService;

public class Main {
    public static void main(String[] args) {

        BinService binservice = new BinService();
        TruckService truckService = new TruckService();

        Bin bin1 =  new Bin( "Manouba" ,"plastic" ,50, "au depot");
       //binservice.add(bin1);
        //binservice.delete(bin1);

       // binservice.update(new Bin(7,"ben arous", "organique", 240.0, "en repos"));
       // System.out.println(binservice.get());

        Truck truck1 = new Truck( 200.0, 2.0, "centre ville", "au dépôt");
       // truckService.add(truck1);
       // truckService.delete(truck1);
        //truckService.update(new Truck(2,202.0,2.3, "Ariana","rempli"));
        System.out.println(truckService.get());






    }
}
