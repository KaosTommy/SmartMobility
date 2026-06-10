package com.smartmobility.backend.business;

import com.smartmobility.backend.model.Utente;
import com.smartmobility.backend.repository.UtenteDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestoreWallet {

    private final UtenteDAO UtenteDAO;


    public GestoreWallet(UtenteDAO UtenteDAO) {
        this.UtenteDAO = UtenteDAO;
    }

    @Transactional
    public float ricaricaWallet(String idUtente, float importo) {
        if (importo <= 0) {
            throw new IllegalArgumentException("L'importo della ricarica deve essere maggiore di zero.");
        }

        Utente utente = UtenteDAO.findById(idUtente)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Qui in produzione ci sarebbe la chiamata al Gateway Bancario (PayPal/Stripe) per prelevare i soldi veri
        
        utente.aggiungiFondi(importo);
        UtenteDAO.save(utente);
        
        return utente.getSaldoWallet();
    }

    public float getSaldo(String idUtente) {
        Utente utente = UtenteDAO.findById(idUtente)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        return utente.getSaldoWallet();
    }
}