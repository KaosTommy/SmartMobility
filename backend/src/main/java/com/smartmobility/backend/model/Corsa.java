package com.smartmobility.backend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "corse")
public class Corsa {
    
    @Id
    private String idCorsa;
    
    private String statoCorsa;
    private Timestamp timeInizio;
    private Timestamp timeFine;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @ManyToOne
    @JoinColumn(name = "id_targa")
    private Mezzo mezzo;

    public Corsa() {}

    public Corsa(String idCorsa, Utente utente, Mezzo mezzo, Timestamp inizio) {
        this.idCorsa = idCorsa;
        this.utente = utente;
        this.mezzo = mezzo;
        this.timeInizio = inizio;
        this.statoCorsa = "Attiva";
    }

    public Mezzo getMezzo() { return mezzo; }
    public void setStatoCorsa(String statoCorsa) { this.statoCorsa = statoCorsa; }
    public void setTimeFine(Timestamp timeFine) { this.timeFine = timeFine; }
    public String getIdCorsa() { return idCorsa; }
public Utente getUtente() { return utente; }
// Nuovi metodi per IF-11: Pausa Corsa
    public String getStatoCorsa() { return statoCorsa; }
    
    public void mettiInPausa() {
        if ("Conclusa".equals(this.statoCorsa)) throw new IllegalStateException("La corsa è già terminata.");
        this.statoCorsa = "In Pausa";
    }

    public void riprendiCorsa() {
        if (!"In Pausa".equals(this.statoCorsa)) throw new IllegalStateException("La corsa non è in pausa.");
        this.statoCorsa = "Attiva";
    }
    public java.sql.Timestamp getTimeInizio() { return timeInizio; }
public java.sql.Timestamp getTimeFine() { return timeFine; }
private int durataPausa = 0;

    public int getDurataPausa() { return durataPausa; }
    public void setDurataPausa(int durataPausa) { this.durataPausa = durataPausa; }

    // IF-9: Metodo richiesto dal Class Diagram
    public void aggiungiPausa(int minuti) {
        this.durataPausa += minuti;
    }
}