package com.smartmobility.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "codici_promozionali")
public class CodicePromozionale {

    @Id
    private String codice;
    private float valoreSconto;
    private String stato; // "ATTIVO", "RISCATTATO"

    public CodicePromozionale() {}

    public CodicePromozionale(String codice, float valoreSconto) {
        this.codice = codice;
        this.valoreSconto = valoreSconto;
        this.stato = "ATTIVO";
    }

    public String getCodice() { return codice; }
    public float getValoreSconto() { return valoreSconto; }
    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
}