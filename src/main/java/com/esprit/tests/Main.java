package com.esprit.tests;

import com.esprit.Services.EventService;
import com.esprit.Services.PosterService;
import com.esprit.models.Event;
import com.esprit.models.Poster;
import com.esprit.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
       /* EventService eventService = new EventService();


        System.out.println("Test 1: Create event with valid organizer (no participants)");
        Event event1 = new Event();
        event1.setName("Event Without Participants");
        event1.setDescription("Event created without participants.");
        event1.setDateTime(LocalDateTime.now().plusDays(1));
        event1.setAddress("123 Main Street");

        User organizer1 = new User();
        organizer1.setId(1);
        event1.setOrganizer(organizer1);
        event1.setParticipants(new ArrayList<>());
        eventService.add(event1);
        System.out.println("Created event: " + event1);

        System.out.println("\nTest 2: Create event with valid organizer and participants");
        Event event2 = new Event();
        event2.setName("Event With Participants");
        event2.setDescription("Event created with participants.");
        event2.setDateTime(LocalDateTime.now().plusDays(2));
        event2.setAddress("456 Park Avenue");

        User organizer2 = new User();
        organizer2.setId(1);
        event2.setOrganizer(organizer2);
        List<User> participants = new ArrayList<>();
        User part1 = new User();
        part1.setId(2);
        User part2 = new User();
        part2.setId(3);
        participants.add(part1);
        participants.add(part2);
        event2.setParticipants(participants);
        eventService.add(event2);
        System.out.println("Created event: " + event2);

        System.out.println("\nTest 3: Create event with invalid organizer");
        Event event3 = new Event();
        event3.setName("Event With Invalid Organizer");
        event3.setDescription("This event should fail due to invalid organizer.");
        event3.setDateTime(LocalDateTime.now().plusDays(3));
        event3.setAddress("789 Broadway");

        User invalidOrganizer = new User();
        invalidOrganizer.setId(99);
        event3.setOrganizer(invalidOrganizer);
        event3.setParticipants(new ArrayList<>());
        eventService.add(event3);
        System.out.println("Attempted creation of event: " + event3);

        System.out.println("\nTest 4: Update event2");
        event2.setName("Updated Event With Participants");
        event2.setDescription("Updated event description.");
        eventService.update(event2);
        System.out.println("Updated event2: " + event2);

        System.out.println("\nTest 5: Add participant to event1");
        User newParticipant = new User();
        newParticipant.setId(2);
        eventService.addParticipant(event1.getId(), newParticipant);

        List<Event> allEvents = eventService.get();
        for (Event e : allEvents) {
            if (e.getId().equals(event1.getId())) {
                event1 = e;
                break;
            }
        }
        System.out.println("Event1 after adding participant: " + event1);

        System.out.println("\nTest 6: Delete event1");
        eventService.delete(event1);
        allEvents = eventService.get();
        System.out.println("Remaining events:");
        for (Event e : allEvents) {
            System.out.println(e);
        }*/

        PosterService posterService = new PosterService() {
            @Override
            public void delete(Poster poster) {

            }
        };

        User user = new User();
        user.setId(3);

        Event event = new Event();
        event.setId(8);

        /*Poster poster = new Poster();
        poster.setTitle("Affiche Test");
        poster.setDescription("Ceci est une affiche de test");
        poster.setImageUrl("https://image.com/test.jpg");
        poster.setCreatedAt(LocalDateTime.now());
        poster.setEvent(event);
        poster.setCreatedBy(user);

        System.out.println("➕ Ajout du poster...");
        posterService.add(poster);*/

        System.out.println("\n📋 Liste des posters :");
        List<Poster> posters = posterService.get();
        for (Poster p : posters) {
            System.out.println(p);
        }

        if (!posters.isEmpty()) {
            Poster firstPoster = posters.get(0);
            firstPoster.setTitle("Titre modifié");
            firstPoster.setDescription("Description mise à jour");
            System.out.println("\n✏️ Mise à jour du premier poster...");
            posterService.update(firstPoster);
        }

        System.out.println("\n📋 Liste après mise à jour :");
        for (Poster p : posterService.get()) {
            System.out.println(p);
        }


        if (!posters.isEmpty()) {
            Poster toDelete = posters.get(0);
            System.out.println("\n🗑️ Suppression du poster avec ID : " + toDelete.getId());
            posterService.deleteById(4);

        }

        System.out.println("\n📋 Liste après suppression :");
        for (Poster p : posterService.get()) {
            System.out.println(p);
        }



    }
}

