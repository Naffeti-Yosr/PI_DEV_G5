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
    private String name;
    private String description;
    private LocalDateTime dateTime;
    private List<String> participants;
    private String address;
    private String organizer;

}
