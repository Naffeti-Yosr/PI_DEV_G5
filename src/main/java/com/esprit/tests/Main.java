package com.esprit.tests;

import com.esprit.models.Bin;
import com.esprit.models.Truck;
import com.esprit.services.BinService;
import com.esprit.services.TruckService;

public class Main {
    public static void main(String[] args) {

        BinService binservice = new BinService();
        TruckService truckService = new TruckService();

        //Bin bin1 =  new Bin(9, "location1" ,"string" ,25, "statut1");
      //  binservice.add(bin1);
      //  binservice.delete(bin1);
       // binservice.update(new Bin(8,"location9", "organique", 240.0, "statut2"));
       // System.out.println(binservice.get());

        Truck truck1 = new Truck( 200.0, 2.0, "QuartierA", "au dépôt");
        truckService.add(truck1);
        //truckService.delete(new Truck(22, 200.0, 2.0, "QuartierA", "au dépôt"));
        truckService.update(new Truck(23,202.0,2.0, "Quartier D","rempli"));
         System.out.println(truckService.get());
        //truckService.delete(truck1);





    }
}
