package com.esprit.tests;

import com.esprit.Services.EventService;
import com.esprit.models.Event;
import com.esprit.models.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EventService eventService = new EventService();

        // Test 1: Create event with valid organizer (no participants)
        System.out.println("Test 1: Create event with valid organizer (no participants)");
        Event event1 = new Event();
        event1.setName("Event Without Participants");
        event1.setDescription("Event created without participants.");
        event1.setDateTime(LocalDateTime.now().plusDays(1));
        event1.setAddress("123 Main Street");
        // Set organizer as a User object (assumed to exist with id=1)
        User organizer1 = new User();
        organizer1.setId(1);
        event1.setOrganizer(organizer1);
        event1.setParticipants(new ArrayList<>());
        eventService.add(event1);
        System.out.println("Created event: " + event1);

        // Test 2: Create event with valid organizer and participants
        System.out.println("\nTest 2: Create event with valid organizer and participants");
        Event event2 = new Event();
        event2.setName("Event With Participants");
        event2.setDescription("Event created with participants.");
        event2.setDateTime(LocalDateTime.now().plusDays(2));
        event2.setAddress("456 Park Avenue");
        // Set organizer as a User object (assumed to exist with id=1)
        User organizer2 = new User();
        organizer2.setId(1);
        event2.setOrganizer(organizer2);
        List<User> participants = new ArrayList<>();
        User part1 = new User();
        part1.setId(2);  // Assumed to exist
        User part2 = new User();
        part2.setId(3);  // Assumed to exist
        participants.add(part1);
        participants.add(part2);
        event2.setParticipants(participants);
        eventService.add(event2);
        System.out.println("Created event: " + event2);

        // Test 3: Attempt to create event with invalid organizer
        System.out.println("\nTest 3: Create event with invalid organizer");
        Event event3 = new Event();
        event3.setName("Event With Invalid Organizer");
        event3.setDescription("This event should fail due to invalid organizer.");
        event3.setDateTime(LocalDateTime.now().plusDays(3));
        event3.setAddress("789 Broadway");
        // Set organizer as a User object with an id that doesn't exist (assumed id=99)
        User invalidOrganizer = new User();
        invalidOrganizer.setId(99);
        event3.setOrganizer(invalidOrganizer);
        event3.setParticipants(new ArrayList<>());
        eventService.add(event3);
        System.out.println("Attempted creation of event: " + event3);

        // Test 4: Update event2
        System.out.println("\nTest 4: Update event2");
        event2.setName("Updated Event With Participants");
        event2.setDescription("Updated event description.");
        eventService.update(event2);
        System.out.println("Updated event2: " + event2);

        // Test 5: Add a participant to event1 after creation
        System.out.println("\nTest 5: Add participant to event1");
        User newParticipant = new User();
        newParticipant.setId(2);  // Assumed to exist
        eventService.addParticipant(event1.getId(), newParticipant);
        // Refresh event1 from the database
        List<Event> allEvents = eventService.get();
        for (Event e : allEvents) {
            if (e.getId().equals(event1.getId())) {
                event1 = e;
                break;
            }
        }
        System.out.println("Event1 after adding participant: " + event1);

        // Test 6: Delete event1
        System.out.println("\nTest 6: Delete event1");
        eventService.delete(event1);
        allEvents = eventService.get();
        System.out.println("Remaining events:");
        for (Event e : allEvents) {
            System.out.println(e);
        }
    }
    }

