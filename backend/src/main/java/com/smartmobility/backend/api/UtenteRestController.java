package com.smartmobility.backend.api;

import com.smartmobility.backend.api.dto.TicketRequest;
import com.smartmobility.backend.business.*;
import com.smartmobility.backend.model.*;
import com.smartmobility.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/utenti")
@CrossOrigin(origins = "*")
public class UtenteRestController {

    private final GestoreAccount gestoreAccount;
    private final CorsaRepository corsaRepository;
    private final UtenteRepository utenteRepository;
    private final GestoreAssistenza gestoreAssistenza; // Aggiunto per connettere l'assistenza

    @Autowired
    public UtenteRestController(GestoreAccount gestoreAccount, CorsaRepository corsaRepository, 
                                UtenteRepository utenteRepository, GestoreAssistenza gestoreAssistenza) {
        this.gestoreAccount = gestoreAccount;
        this.corsaRepository = corsaRepository;
        this.utenteRepository = utenteRepository;
        this.gestoreAssistenza = gestoreAssistenza;
    }

    @PostMapping("/registra")
    public ResponseEntity<?> registra(@RequestBody Utente utente) {
        try {
            if(utente.getId() == null) utente.setId("U_" + System.currentTimeMillis());
            Utente registrato = gestoreAccount.registraUtente(utente);
            return ResponseEntity.ok("Utente " + registrato.getNome() + " registrato con successo!");
        } catch (Exception e) { return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); }
    }

    @GetMapping("/{idUtente}/storico")
    public ResponseEntity<List<Corsa>> getStoricoCorse(@PathVariable String idUtente) {
        try {
            Utente u = utenteRepository.findById(idUtente).orElseThrow();
            return ResponseEntity.ok(corsaRepository.findByUtente(u));
        } catch (Exception e) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); }
    }

    // NUOVO IF-22: Endpoint per ricevere la segnalazione dall'app
    @PostMapping("/{idUtente}/ticket")
    public ResponseEntity<String> inviaSegnalazione(@PathVariable String idUtente, @RequestBody TicketRequest request) {
        try {
            gestoreAssistenza.apriTicketUtente(idUtente, request.tipologia(), request.descrizione());
            return ResponseEntity.ok("Segnalazione inviata al reparto manutenzione.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore di sistema: " + e.getMessage());
        }
    }
    @Autowired
    private com.smartmobility.backend.repository.MultaRepository multaRepository;

    // IF-30: Endpoint per visualizzare le sanzioni amministrative dell'utente
    @GetMapping("/{idUtente}/multe")
    public ResponseEntity<List<com.smartmobility.backend.model.Multa>> getMulteUtente(@PathVariable String idUtente) {
        try {
            Utente u = utenteRepository.findById(idUtente).orElseThrow();
            return ResponseEntity.ok(multaRepository.findByUtente(u));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}