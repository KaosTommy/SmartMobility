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
    private final UtenteRepository utenteRepository;
    private final ISensorAPI sensoriIoT;
    private final GestoreFlotta gestoreFlotta;

    
    public GestoreNoleggio(CorsaRepository corsaRepository, MezzoRepository mezzoRepository, 
                           UtenteRepository utenteRepository, 
                           ISensorAPI sensoriIoT, GestoreFlotta gestoreFlotta) {
        this.corsaRepository = corsaRepository;
        this.mezzoRepository = mezzoRepository;
        this.utenteRepository = utenteRepository;
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
        Utente utente = utenteRepository.findById(idUtente).orElseThrow();
        Mezzo mezzo = mezzoRepository.findById(idMezzo).orElseThrow();

        if ("Prenotato".equals(mezzo.getStatoOperativo())) {
            if (!idUtente.equals(mezzo.getIdUtentePrenotante())) throw new IllegalStateException("Mezzo prenotato da un altro utente!");
        } else if (!"Disponibile".equals(mezzo.getStatoOperativo())) {
            throw new IllegalStateException("Il mezzo non è disponibile.");
        }

        if (!sensoriIoT.inviaComandoSblocco(mezzo.getIdSensore())) throw new RuntimeException("Errore IoT");

        mezzo.setStatoOperativo("In Uso");
        mezzo.setIdUtentePrenotante(null); 
        mezzo.setScadenzaPrenotazione(null);
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
        corsa.setTimeFine(new Timestamp(System.currentTimeMillis()));
        corsaRepository.save(corsa);

        mezzo.setStatoOperativo("Disponibile");
        mezzoRepository.save(mezzo);

        return new Ricevuta("REC_" + System.currentTimeMillis(), 2.50f);
    }
    // IF-13
    public List<Corsa> estraiStoricoCorse(String idUtente) {
        return corsaRepository.findByUtente_Id(idUtente);
    }
}