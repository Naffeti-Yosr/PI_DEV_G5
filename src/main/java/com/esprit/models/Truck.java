package com.esprit.models;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Truck {
    private int id;
    private double capaciteMax;
    private double niveauRemplissageActuel;
    private String section;
    private String statut;


}
