package com.smartmobility.backend.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Coordinate {
    private double latitudine;
    private double longitudine;

    public Coordinate() {} // Costruttore vuoto per JPA

    public Coordinate(double lat, double lon) {
        this.latitudine = lat;
        this.longitudine = lon;
    }

    public double getLatitudine() { return latitudine; }
    public double getLongitudine() { return longitudine; }
}