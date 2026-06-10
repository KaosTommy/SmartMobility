package com.smartmobility.backend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "mezzi")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Mezzo {
    
    @Id
    private String idTarga;
    @Column(unique = true)
    private String idSensore;
    private String statoOperativo; 
    
    private String idUtentePrenotante;
    private Timestamp scadenzaPrenotazione;
    private int livelloBatteria;

    // Aggiunte le coordinate per la mappa!
    private double latitudine;
    private double longitudine;

    public Mezzo() {}

    public Mezzo(String idTarga, String idSensore) {
        this.idTarga = idTarga;
        this.idSensore = idSensore;
        this.statoOperativo = "Disponibile";
        this.livelloBatteria = 100;
    }

    public String getIdTarga() { return idTarga; }
    public String getIdSensore() { return idSensore; }
    public String getStatoOperativo() { return statoOperativo; }
    public void setStatoOperativo(String stato) { this.statoOperativo = stato; }
    public String getIdUtentePrenotante() { return idUtentePrenotante; }
    public void setIdUtentePrenotante(String id) { this.idUtentePrenotante = id; }
    public Timestamp getScadenzaPrenotazione() { return scadenzaPrenotazione; }
    public void setScadenzaPrenotazione(Timestamp scadenza) { this.scadenzaPrenotazione = scadenza; }
    public int getLivelloBatteria() { return livelloBatteria; }
    public void setLivelloBatteria(int livelloBatteria) { this.livelloBatteria = livelloBatteria; }
    
    // Metodi per far felice il DataSeeder
    public double getLatitudine() { return latitudine; }
    public void setLatitudine(double latitudine) { this.latitudine = latitudine; }
    public double getLongitudine() { return longitudine; }
    public void setLongitudine(double longitudine) { this.longitudine = longitudine; }
}