package com.smartmobility.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "metodi_pagamento")
public class MetodoPagamento {

    @Id
    private String idMetodo;
    private String numeroCartaOscurato; // Es. **** **** **** 1234
    private String intestatario;
    private String scadenza; // Formato MM/YY

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

    public MetodoPagamento() {}

    public MetodoPagamento(String idMetodo, String numeroCartaOscurato, String intestatario, String scadenza, Utente utente) {
        this.idMetodo = idMetodo;
        this.numeroCartaOscurato = numeroCartaOscurato;
        this.intestatario = intestatario;
        this.scadenza = scadenza;
        this.utente = utente;
    }

    public String getIdMetodo() { return idMetodo; }
    public String getNumeroCartaOscurato() { return numeroCartaOscurato; }
    public String getIntestatario() { return intestatario; }
    public String getScadenza() { return scadenza; }
}