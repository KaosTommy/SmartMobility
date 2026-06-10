package com.smartmobility.backend.business;

import com.smartmobility.backend.api.dto.DashboardStats;
import com.smartmobility.backend.model.Utente;
import com.smartmobility.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestoreAdmin {

    private final UtenteRepository utenteRepository;
    private final MezzoRepository mezzoRepository;
    private final CorsaRepository corsaRepository;

    @Autowired
    public GestoreAdmin(UtenteRepository utenteRepository, MezzoRepository mezzoRepository, CorsaRepository corsaRepository) {
        this.utenteRepository = utenteRepository;
        this.mezzoRepository = mezzoRepository;
        this.corsaRepository = corsaRepository;
    }

    // Estrazione KPI aziendali per la BI
    public DashboardStats calcolaStatistiche() {
        long totaleUtenti = utenteRepository.count();
        long corseTotali = corsaRepository.count();
        
        long disponibili = mezzoRepository.findAll().stream().filter(m -> "Disponibile".equals(m.getStatoOperativo())).count();
        long inUso = mezzoRepository.findAll().stream().filter(m -> "In Uso".equals(m.getStatoOperativo())).count();
        long inManutenzione = mezzoRepository.findAll().stream().filter(m -> "In Manutenzione".equals(m.getStatoOperativo())).count();
        
        // Calcolo stimato basato sulla tariffa flat di 2.50€ cablata nello Sprint 2
        float incassoStimato = corseTotali * 2.50f;

        return new DashboardStats(totaleUtenti, disponibili, inUso, inManutenzione, corseTotali, incassoStimato);
    }

    // Gestione Sicurezza: Sospensione Account
    @Transactional
    public void cambiaStatoUtente(String idUtente, String nuovoStato) {
        Utente utente = utenteRepository.findById(idUtente)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        
        utente.setStatoAccount(nuovoStato);
        utenteRepository.save(utente);
    }
}