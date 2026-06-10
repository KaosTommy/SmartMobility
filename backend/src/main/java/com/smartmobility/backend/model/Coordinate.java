package com.smartmobility.backend.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Coordinate {
    private double latitudine;
    private double longitudine;

    public Coordinate() {}

    public Coordinate(double latitudine, double longitudine) {
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public double getLatitudine() { return latitudine; }
    public void setLatitudine(double latitudine) { this.latitudine = latitudine; }
    public double getLongitudine() { return longitudine; }
    public void setLongitudine(double longitudine) { this.longitudine = longitudine; }
}