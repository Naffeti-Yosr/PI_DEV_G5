package com.esprit.tests;

import com.esprit.models.Client;
import com.esprit.models.User;
import com.esprit.services.ClientService;
import com.esprit.services.UserService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserService();
        //User u1 = new User(1,"wael","chenoufi", Date.from(LocalDate.of(1999, 9, 13).atStartOfDay(ZoneId.systemDefault()).toInstant()), "wael@chenoufi@esprit.tn", "w@el2025", "admin");
        //userService.add(u1);
       // User u2 = new User(3, "malek", "hamdi", Date.from(LocalDate.of(2004, 1, 16).atStartOfDay(ZoneId.systemDefault()).toInstant()), "hamdi@malek@esprit.tn", "Malek", "visiteur");
        //userService.add(u2);
        //userService.delete(u2);
        //System.out.println(userService.get());
        ClientService clientService = new ClientService();
        Client client = new Client(
                0,
                "Ahmed",
                "Ben Ali",
                Date.from(LocalDate.of(2004, 1, 16).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                "ahmed.benali@example.com",
                "securepassword123",
                "client",
                "123 Rue Habib Bourguiba, Tunis",
                "22556677"
        );
        clientService.add(client);
        System.out.println(clientService.get());


    }
}
