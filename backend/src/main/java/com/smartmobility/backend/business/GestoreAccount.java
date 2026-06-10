package com.smartmobility.backend.business;

import com.smartmobility.backend.model.Utente;
import com.smartmobility.backend.repository.UtenteDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestoreAccount {

    private final UtenteDAO UtenteDAO;

  
    public GestoreAccount(UtenteDAO UtenteDAO) {
        this.UtenteDAO = UtenteDAO;
    }

    // IF-1: Registrazione nuovo utente
    @Transactional
 public boolean registraUtente(Utente datiUtente) {
        // Ramo UML: [se DatiErrati]
        if (datiUtente.getEmail() == null || !datiUtente.getEmail().contains("@") || datiUtente.getPasswordHash().length() < 5) {
            throw new IllegalArgumentException("Formato non valido"); // Questo fa fallire l'operazione
        }

        // Ramo UML: [se DatiEsistenti]
        if (UtenteDAO.findByEmail(datiUtente.getEmail()).isPresent()) {
            throw new IllegalStateException("Dati già in uso");
        }

        // Ramo UML: [se DatiCorretti]
        UtenteDAO.save(datiUtente);
        return true; 
    }

   // IF-2: Autenticazione (Estrae da DB e poi controlla password come da UML)
    public Utente autenticaUtente(String email, String password) {
        Utente utente = UtenteDAO.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        
        if (!utente.getPasswordHash().equals(password)) {
            throw new RuntimeException("Credenziali errate");
        }
        return utente;
    }

    // IF-3, IF-18, IF-23: Disconnessione (Metodo per tracciabilità UML)
    public void disconnettiUtente(String idSessione) {
        // In architettura REST Stateless non manteniamo la sessione sul server,
        // ma il metodo è presente per totale coerenza con il Sequence Diagram.
        System.out.println("Sessione " + idSessione + " invalidata logicamente.");
    }
    // IF-16: Eliminazione account e anonimizzazione dati (GDPR)
    @Transactional
    public void eliminaAccount(String idUtente) {
        Utente utente = UtenteDAO.findById(idUtente)
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
        UtenteDAO.save(utente);
    }
}