package com.smartmobility.backend.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Poligono {
    private String coordinateJSON; // Simulazione della struttura spaziale complessa

    public Poligono() {}
    public Poligono(String coordinateJSON) { this.coordinateJSON = coordinateJSON; }
    
    public String getCoordinateJSON() { return coordinateJSON; }
    public void setCoordinateJSON(String coordinateJSON) { this.coordinateJSON = coordinateJSON; }
}