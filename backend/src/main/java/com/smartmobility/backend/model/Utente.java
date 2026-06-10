package com.smartmobility.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "utenti")
public class Utente {
    
    @Id
    private String id;
    private String nome;
    private String cognome;
    
    @Column(unique = true)
    private String email;
    
    // NUOVI CAMPI PER L'AUTENTICAZIONE 1:1 CON IL DOCUMENTO
    private String passwordHash;
    private String ruolo; // "UTENTE", "OPERATORE", "ADMIN"
    
    private String statoAccount; 
    private float saldoWallet;
    private boolean patenteBValida;

    public Utente() {}

    public Utente(String id, String nome, String cognome, String email, String passwordHash, String ruolo) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.passwordHash = passwordHash;
        this.ruolo = ruolo;
        this.statoAccount = "ABILITATO";
        this.saldoWallet = 0.0f;
        this.patenteBValida = false;
    }

    // --- GETTER E SETTER ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRuolo() { return ruolo; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }
    
    public String getStatoAccount() { return statoAccount; }
    public void setStatoAccount(String statoAccount) { this.statoAccount = statoAccount; }
    public float getSaldoWallet() { return saldoWallet; }
    public void aggiungiFondi(float importo) { this.saldoWallet += importo; }
    public boolean isPatenteBValida() { return patenteBValida; }
    public void setPatenteBValida(boolean patenteBValida) { this.patenteBValida = patenteBValida; }
}