package com.smartmobility.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "hub_ricarica")
public class HubRicarica {

    @Id
    private String idHub;
    private String nome;
    private double latitudine;
    private double longitudine;
    private int capacitaMassima;
    private int postiDisponibili;

    public HubRicarica() {}

    public HubRicarica(String idHub, String nome, double latitudine, double longitudine, int capacitaMassima) {
        this.idHub = idHub;
        this.nome = nome;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.capacitaMassima = capacitaMassima;
        this.postiDisponibili = capacitaMassima; // All'inizio tutti i posti sono liberi
    }

    public String getIdHub() { return idHub; }
    public String getNome() { return nome; }
    public double getLatitudine() { return latitudine; }
    public double getLongitudine() { return longitudine; }
    public int getCapacitaMassima() { return capacitaMassima; }
    public int getPostiDisponibili() { return postiDisponibili; }
    
    public void occupaPosto() {
        if (postiDisponibili > 0) postiDisponibili--;
    }
    
    public void liberaPosto() {
        if (postiDisponibili < capacitaMassima) postiDisponibili++;
    }
}