package com.esprit.models;

import java.time.LocalDateTime;

public class Reservation {
    private int id;
    private User user;
    private Evenement event;
    private LocalDateTime dateReservation;

    public Reservation() {}

    public Reservation(int id, User user, Evenement event, LocalDateTime dateReservation) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.dateReservation = dateReservation;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Evenement getEvent() {
        return event;
    }

    public void setEvent(Evenement event) {
        this.event = event;
    }

    public LocalDateTime getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(LocalDateTime dateReservation) {
        this.dateReservation = dateReservation;
    }
}
