package com.smartmobility.backend.business;

import com.smartmobility.backend.model.Utente;
import com.smartmobility.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestoreAccount {

    private final UtenteRepository utenteRepository;

    @Autowired
    public GestoreAccount(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    // IF-1: Registrazione nuovo utente
    @Transactional
    public Utente registraUtente(Utente nuovoUtente) {
        // Controllo di sicurezza: l'email esiste già?
        if (utenteRepository.findByEmail(nuovoUtente.getEmail()).isPresent()) {
            throw new IllegalStateException("Email già in uso nel sistema.");
        }
        
        // Impostiamo lo stato di default
        nuovoUtente.setStatoAccount("ABILITATO");
        return utenteRepository.save(nuovoUtente);
    }

    // IF-2: Login
    public Utente autenticaUtente(String email, String passwordHash) {
        Utente utente = utenteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
                
        // Nota accademica: In produzione qui verificheremmo l'hash della password.
        // Nel prototipo diamo l'ok se l'email esiste.
        return utente;
    }
    // IF-16: Eliminazione account e anonimizzazione dati (GDPR)
    @Transactional
    public void eliminaAccount(String idUtente) {
        Utente utente = utenteRepository.findById(idUtente)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if ("ELIMINATO".equals(utente.getStatoAccount())) {
            throw new IllegalStateException("L'account è già stato eliminato.");
        }

        // Anonimizzazione dei dati personali
        utente.setNome("Utente");
        utente.setCognome("Anonimizzato");
        // Cambiamo l'email per "liberarla" e renderla irriconoscibile
        utente.setEmail("deleted_" + utente.getId() + "@anon.local"); 
        utente.setStatoAccount("ELIMINATO");
        
        // Salviamo l'utente anonimizzato. 
        // Le sue corse e il saldo restano nel DB per i bilanci aziendali, ma non sono più riconducibili a lui.
        utenteRepository.save(utente);
    }
}