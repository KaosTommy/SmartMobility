package com.smartmobility.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stazioni_ricarica")
public class StazioneRicarica {
    
    @Id
    private String id;
    private String nome;
    private double latitudine;
    private double longitudine;
    private int capienza;

    public StazioneRicarica() {}

    public StazioneRicarica(String id, String nome, double latitudine, double longitudine, int capienza) {
        this.id = id;
        this.nome = nome;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.capienza = capienza;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public double getLatitudine() { return latitudine; }
    public double getLongitudine() { return longitudine; }
    public int getCapienza() { return capienza; }
}