package com.esprit.tests;

import com.esprit.Services.EvenementService;
import com.esprit.Services.PosterService;
import com.esprit.models.Evenement;
import com.esprit.models.Poster;
import com.esprit.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        User organizer = new User();
        organizer.setId(1);
        organizer.setNom("Organizer");
        organizer.setPrenom("One");
        organizer.setEmail("organizer@example.com");
        organizer.setPassword("password");
        organizer.setRole("ROLE_ORGANIZER");

        organizer.setBirthDate(LocalDateTime.now().minusYears(30));

        User participant = new User();
        participant.setId(2);
        participant.setNom("Participant");
        participant.setPrenom("Two");
        participant.setEmail("participant@example.com");
        participant.setPassword("password");
        participant.setRole("ROLE_PARTICIPANT");
        participant.setBirthDate(LocalDateTime.now().minusYears(25));

        Evenement event = new Evenement();
        event.setTitre("Sample Event");
        event.setDescription("This is a test event");
        event.setDate(LocalDateTime.now());
        event.setAdresse("123 Main Street, City");
        event.setOrganisateur(organizer);

        List<User> participants = new ArrayList<>();
        participants.add(participant);
        event.setParticipants(participants);

        EvenementService evenementService = new EvenementService();

        System.out.println("Adding event...");
        evenementService.add(event);

        List<Evenement> events = evenementService.get();
        System.out.println("List of events in DB:");
        for (Evenement e : events) {
            System.out.println("Event ID: " + e.getId() + ", Title: " + e.getTitre() +
                    ", Description: " + e.getDescription());
        }

        Evenement ev = evenementService.getEvent(1);
        System.out.println("List of events in DB:");

            System.out.println("Event ID: " + ev.getId() + ", Title: " + ev.getTitre() +
                    ", Description: " + ev.getDescription());




        event.setDescription("Updated event description");
        System.out.println("Updating event with ID " + event.getId());
        evenementService.update(event);

        // evenementService.delete(event.getId());
        // System.out.println("Deleted event with ID " + event.getId());

        PosterService posterService = new PosterService() {
        };

        Poster poster = new Poster();
        poster.setTitre("Event Poster");
        poster.setDescription("Poster description");
        poster.setImageUrl("http://example.com/image.jpg");
        poster.setCreatedAt(LocalDateTime.now());
        poster.setEvenementId(event.getId());
        poster.setCreatedById(organizer.getId());

        System.out.println("Adding poster...");
        posterService.add(poster);

        List<Poster> posters = posterService.get();
        System.out.println("List of posters in DB:");
        for (Poster p : posters) {
            System.out.println("Poster ID: " + p.getId() + ", Title: " + p.getTitre() +
                    ", Image URL: " + p.getImageUrl());
        }

        poster.setDescription("Updated poster description");
        System.out.println("Updating poster with ID " + poster.getId());
        posterService.update(poster);

        System.out.println("Deleting poster with ID " + poster.getId());
        posterService.delete(poster.getId());
        System.out.println("Poster deleted successfully.");

        Poster poster1 = new Poster(1, "/images/event1.png");

        Evenement evenement = new Evenement();
        evenement.setPoster(poster1);

        if (evenement.getPoster() != null) {
            System.out.println("✅ getPoster() fonctionne !");
            System.out.println("Image path : " + evenement.getPoster().getImagePath());
        } else {
            System.out.println("❌ getPoster() ne retourne rien !");
        }
    }
    }
