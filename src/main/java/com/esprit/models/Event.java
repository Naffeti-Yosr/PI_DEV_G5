package com.esprit.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor

@Getter
@Setter

public class Event {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private List<User> participants;
    private String address;
    private User organizer;

}
