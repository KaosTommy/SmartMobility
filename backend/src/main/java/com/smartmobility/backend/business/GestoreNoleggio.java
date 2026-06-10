package com.smartmobility.backend.business;

import com.smartmobility.backend.integration.ISensorAPI;
import com.smartmobility.backend.model.*;
import com.smartmobility.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Service
public class GestoreNoleggio {

    private final CorsaRepository corsaRepository;
    private final MezzoRepository mezzoRepository;
    private final UtenteDAO UtenteDAO;
    private final ISensorAPI sensoriIoT;
    private final GestoreFlotta gestoreFlotta;

    
    public GestoreNoleggio(CorsaRepository corsaRepository, MezzoRepository mezzoRepository, 
                           UtenteDAO UtenteDAO, 
                           ISensorAPI sensoriIoT, GestoreFlotta gestoreFlotta) {
        this.corsaRepository = corsaRepository;
        this.mezzoRepository = mezzoRepository;
        this.UtenteDAO = UtenteDAO;
        this.sensoriIoT = sensoriIoT;
        this.gestoreFlotta = gestoreFlotta;
    }

    @Transactional
    public void prenotaMezzo(String idUtente, String idMezzo) {
        Mezzo mezzo = mezzoRepository.findById(idMezzo).orElseThrow(() -> new RuntimeException("Mezzo non trovato"));
        
        if ("Prenotato".equals(mezzo.getStatoOperativo()) && mezzo.getScadenzaPrenotazione() != null && 
            mezzo.getScadenzaPrenotazione().before(new Timestamp(System.currentTimeMillis()))) {
            mezzo.setStatoOperativo("Disponibile"); 
        }

        if (!"Disponibile".equals(mezzo.getStatoOperativo())) {
            throw new IllegalStateException("Il mezzo non è attualmente disponibile per la prenotazione.");
        }

        mezzo.setStatoOperativo("Prenotato");
        mezzo.setIdUtentePrenotante(idUtente);
        mezzo.setScadenzaPrenotazione(new Timestamp(System.currentTimeMillis() + (15 * 60 * 1000)));
        mezzoRepository.save(mezzo);
    }

    @Transactional
    public String avviaCorsa(String idUtente, String idMezzo) {
        Utente utente = UtenteDAO.findById(idUtente).orElseThrow();
        Mezzo mezzo = mezzoRepository.findById(idMezzo).orElseThrow();

        if ("Prenotato".equals(mezzo.getStatoOperativo())) {
            if (!idUtente.equals(mezzo.getIdUtentePrenotante())) throw new IllegalStateException("Mezzo prenotato da un altro utente!");
        } else if (!"Disponibile".equals(mezzo.getStatoOperativo())) {
            throw new IllegalStateException("Il mezzo non è disponibile.");
        }

        // ---> INIZIO FIX: Guardia di Sicurezza per la Patente <---
        // Sostituisci "getTipo()" e "isPatenteBValida()" con i nomi esatti dei metodi che avete in Utente e Mezzo.
        // Se avete usato Stringhe per il tipo (es. "Motociclo"), usa .equals("Motociclo")
        if (("Motociclo".equalsIgnoreCase(mezzo.getTipo()) || "Automobile".equalsIgnoreCase(mezzo.getTipo())) 
             && !utente.isPatenteBValida()) {
            throw new IllegalStateException("Azione negata: Patente non valida o assente per questo veicolo.");
        }
        // ---> FINE FIX <---

        if (!sensoriIoT.inviaComandoSblocco(mezzo.getIdSensore())) throw new RuntimeException("Errore IoT");

        mezzo.setStatoOperativo("In Uso");
        mezzo.setIdUtentePrenotante(null); 
        mezzo.setScadenzaPrenotazione(null);
        mezzo.aggiornaTelemetria(mezzo.getCoordinateAttuali(), mezzo.getLivelloBatteria() - 1);
        mezzoRepository.save(mezzo);

        Corsa nuovaCorsa = new Corsa("C_" + System.currentTimeMillis(), utente, mezzo, new Timestamp(System.currentTimeMillis()));
        corsaRepository.save(nuovaCorsa);
        return nuovaCorsa.getIdCorsa();
    }

    @Transactional
    public void mettiInPausaCorsa(String idCorsa) {
        Corsa corsa = corsaRepository.findById(idCorsa).orElseThrow(() -> new RuntimeException("Corsa non trovata"));
        if (!sensoriIoT.inviaComandoBlocco(corsa.getMezzo().getIdSensore())) throw new RuntimeException("Errore IoT");
        corsa.mettiInPausa();
        
        corsaRepository.save(corsa);
    }

    @Transactional
    public void riprendiCorsaInPausa(String idCorsa) {
        Corsa corsa = corsaRepository.findById(idCorsa).orElseThrow(() -> new RuntimeException("Corsa non trovata"));
        if (!sensoriIoT.inviaComandoSblocco(corsa.getMezzo().getIdSensore())) throw new RuntimeException("Errore IoT");
        corsa.riprendiCorsa();
        corsaRepository.save(corsa);
    }

   @Transactional
    public Ricevuta terminaCorsa(String idCorsa) {
        Corsa corsa = corsaRepository.findById(idCorsa).orElseThrow(() -> new RuntimeException("Corsa non trovata"));
        Mezzo mezzo = corsa.getMezzo();

        Coordinate posizioneChiusura = sensoriIoT.rilevaPosizione(mezzo.getIdSensore());
        if (!gestoreFlotta.verificaAreaGeofencing(posizioneChiusura)) {
            throw new IllegalStateException("Attenzione: Sei in un'area di sosta vietata! Sposta il veicolo per terminare la corsa.");
        }

        if (!sensoriIoT.inviaComandoBlocco(mezzo.getIdSensore())) throw new RuntimeException("Errore blocco fisico.");

        corsa.setStatoCorsa("Conclusa");
        // Impostiamo il tempo di fine
        Timestamp tempoFine = new Timestamp(System.currentTimeMillis());
        corsa.setTimeFine(tempoFine);
        corsaRepository.save(corsa);

        mezzo.setStatoOperativo("Disponibile");
        mezzoRepository.save(mezzo);

        // ---> INIZIO FIX: Calcolo Dinamico Tariffa <---
        // 1. Calcola i millisecondi tra inizio e fine corsa (assicurati che getTimeInizio() sia il nome corretto)
        long millisecondiTrascorsi = tempoFine.getTime() - corsa.getTimeInizio().getTime();
        long minutiTrascorsi = millisecondiTrascorsi / (60 * 1000); // Converte in minuti
        
        if (minutiTrascorsi == 0) {
            minutiTrascorsi = 1; // Fa pagare almeno 1 minuto minimo
        }

        // 2. Calcola l'importo (Assicurati che getTariffaAlMinuto() esista. Se non c'è, usa temporaneamente 0.20f come valore dinamico)
        float importoCalcolato = (float) (minutiTrascorsi * mezzo.getTariffaAlMinuto());

        // 3. Genera la ricevuta corretta
        return new Ricevuta("REC_" + System.currentTimeMillis(), importoCalcolato);
        // ---> FINE FIX <---
    }
    // IF-13
    public List<Corsa> estraiStoricoCorse(String idUtente) {
        return corsaRepository.findByUtente_Id(idUtente);
    }
}