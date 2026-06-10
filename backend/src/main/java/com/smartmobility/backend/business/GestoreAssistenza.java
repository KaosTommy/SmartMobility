package com.smartmobility.backend.business;

import com.smartmobility.backend.model.*;
import com.smartmobility.backend.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GestoreAssistenza {

    private final TicketSupportoRepository ticketRepository;
    private final MezzoRepository mezzoRepository;
    private final UtenteDAO UtenteDAO; // Aggiunto per le segnalazioni

   
    public GestoreAssistenza(TicketSupportoRepository ticketRepository, MezzoRepository mezzoRepository, UtenteDAO UtenteDAO) {
        this.ticketRepository = ticketRepository;
        this.mezzoRepository = mezzoRepository;
        this.UtenteDAO = UtenteDAO;
    }

    // IF-29: Recupera i ticket aperti
    public List<TicketSupporto> recuperaTicketAperti() {
        return ticketRepository.findByStato("Aperto");
    }

    // IF-24: Aggiorna lo stato del ticket
    @Transactional
    public void risolviTicket(String idTicket) {
        TicketSupporto ticket = ticketRepository.findById(idTicket).orElseThrow();
        ticket.setStato("Chiuso");
        ticketRepository.save(ticket);
    }

    // IF-26: Metti un mezzo in manutenzione
    @Transactional
    public void impostaManutenzioneMezzo(String idMezzo) {
        Mezzo mezzo = mezzoRepository.findById(idMezzo).orElseThrow();
        if ("In Uso".equals(mezzo.getStatoOperativo())) throw new IllegalStateException("Impossibile mettere in manutenzione un mezzo in corsa!");
        mezzo.setStatoOperativo("In Manutenzione");
        mezzoRepository.save(mezzo);
    }

    // NUOVO IF-22: L'utente segnala un guasto
    @Transactional
    public void apriTicketUtente(String idUtente, String tipologia, String descrizione) {
        Utente utente = UtenteDAO.findById(idUtente).orElseThrow(() -> new RuntimeException("Utente non trovato"));
        TicketSupporto nuovoTicket = new TicketSupporto("TKT_" + System.currentTimeMillis(), tipologia, descrizione, utente);
        ticketRepository.save(nuovoTicket);
    }
    // IF-27: L'operatore identifica i mezzi da ricaricare
    public List<Mezzo> recuperaMezziDaRicaricare() {
        // Restituisce tutti i mezzi con batteria strettamente minore di 10
        return mezzoRepository.findByLivelloBatteriaLessThan(10);
    }
}