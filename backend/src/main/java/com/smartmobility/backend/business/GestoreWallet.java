package com.smartmobility.backend.business;

import com.smartmobility.backend.model.Utente;
import com.smartmobility.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestoreWallet {

    private final UtenteRepository utenteRepository;

    @Autowired
    public GestoreWallet(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    @Transactional
    public float ricaricaWallet(String idUtente, float importo) {
        if (importo <= 0) {
            throw new IllegalArgumentException("L'importo della ricarica deve essere maggiore di zero.");
        }

        Utente utente = utenteRepository.findById(idUtente)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Qui in produzione ci sarebbe la chiamata al Gateway Bancario (PayPal/Stripe) per prelevare i soldi veri
        
        utente.aggiungiFondi(importo);
        utenteRepository.save(utente);
        
        return utente.getSaldoWallet();
    }

    public float getSaldo(String idUtente) {
        Utente utente = utenteRepository.findById(idUtente)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        return utente.getSaldoWallet();
    }
}