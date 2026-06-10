package com.smartmobility.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "zone_geofencing")
public class ZonaGeofencing {
    
    @Id
    private String id;
    
    @Embedded
    private Poligono perimetro;
    private String tipoDivieto; // "ZTL", "Divieto Sosta", "No-Transit"

    public ZonaGeofencing() {}

    public ZonaGeofencing(String id, Poligono perimetro, String tipoDivieto) {
        this.id = id;
        this.perimetro = perimetro;
        this.tipoDivieto = tipoDivieto;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Poligono getPerimetro() { return perimetro; }
    public void setPerimetro(Poligono perimetro) { this.perimetro = perimetro; }
    public String getTipoDivieto() { return tipoDivieto; }
    public void setTipoDivieto(String tipoDivieto) { this.tipoDivieto = tipoDivieto; }
}