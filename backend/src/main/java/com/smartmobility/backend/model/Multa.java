package com.smartmobility.backend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "multe")
public class Multa {

    @Id
    private String idMulta;
    private float importo;
    private String motivazione;
    private Timestamp dataEmissione;
    private String statoPagamento; // "PENDENTE", "PAGATA"

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

    @OneToOne
    @JoinColumn(name = "id_corsa")
    private Corsa corsa;

    public Multa() {}

    public Multa(String idMulta, float importo, String motivazione, Utente utente, Corsa corsa) {
        this.idMulta = idMulta;
        this.importo = importo;
        this.motivazione = motivazione;
        this.utente = utente;
        this.corsa = corsa;
        this.dataEmissione = new Timestamp(System.currentTimeMillis());
        this.statoPagamento = "PENDENTE";
    }

    // --- GETTER E SETTER ---
    public String getIdMulta() { return idMulta; }
    public float getImporto() { return importo; }
    public String getMotivazione() { return motivazione; }
    public String getStatoPagamento() { return statoPagamento; }
    public void setStatoPagamento(String statoPagamento) { this.statoPagamento = statoPagamento; }
public java.sql.Timestamp getDataEmissione() { return dataEmissione; }
}