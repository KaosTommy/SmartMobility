package com.smartmobility.backend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "ticket_supporto")
public class TicketSupporto {

    @Id
    private String idTicket;
    
    private String tipologia;
    private String descrizione;
    private String stato; // Aperto, In Lavorazione, Chiuso
    private Timestamp dataApertura;

    @ManyToOne
    @JoinColumn(name = "id_utente")
    private Utente utente;

    public TicketSupporto() {}

    public TicketSupporto(String idTicket, String tipologia, String descrizione, Utente utente) {
        this.idTicket = idTicket;
        this.tipologia = tipologia;
        this.descrizione = descrizione;
        this.utente = utente;
        this.stato = "Aperto";
        this.dataApertura = new Timestamp(System.currentTimeMillis());
    }

    public String getIdTicket() { return idTicket; }
    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
    public String getTipologia() { return tipologia; }
    public String getDescrizione() { return descrizione; }
public java.sql.Timestamp getDataApertura() { return dataApertura; }
}