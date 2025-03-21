package com.esprit.models;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter

public class Poster {
    private int id;
    private String title;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Event event;
    private User createdBy;

}
