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
    
   
// IF-8: Aggiornamento telemetrico da UML
  @Embedded
    private Coordinate coordinateAttuali;

    public Coordinate getCoordinateAttuali() { return coordinateAttuali; }
    public void setCoordinateAttuali(Coordinate coordinateAttuali) { this.coordinateAttuali = coordinateAttuali; }
    
    // Aggiorna anche il metodo della telemetria per usare l'oggetto:
    public void aggiornaTelemetria(Coordinate coord, int batt) {
        this.coordinateAttuali = coord;
        this.livelloBatteria = batt;
    }
    // ---> INIZIO AGGIUNTE PER RISOLVERE GLI ERRORI IN GESTORE NOLEGGIO <---
    
    // 1. Aggiungiamo la tariffa di base (mettiamo un default di 0.25 cent/minuto per sicurezza)
    @Column(name = "tariffa_al_minuto")
    private double tariffaAlMinuto = 0.25;

    public double getTariffaAlMinuto() {
        return tariffaAlMinuto;
    }

    public void setTariffaAlMinuto(double tariffaAlMinuto) {
        this.tariffaAlMinuto = tariffaAlMinuto;
    }

    // 2. Il tocco di classe OOP: invece di salvare una stringa, usiamo la Reflection
    // per capire automaticamente se l'oggetto è un Motociclo, Automobile o Monopattino
    // in base alla classe figlia che lo ha istanziato.
    public String getTipo() {
        return this.getClass().getSimpleName();
    }
    
    // ---> FINE AGGIUNTE <---
}