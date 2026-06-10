package com.smartmobility.backend.api;

import com.smartmobility.backend.business.GestoreAssistenza;
import com.smartmobility.backend.model.TicketSupporto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operatore")
@CrossOrigin(origins = "*") // Permette la comunicazione con la nostra pagina HTML
public class OperatoreRestController {

    private final GestoreAssistenza gestoreAssistenza;

    @Autowired
    public OperatoreRestController(GestoreAssistenza gestoreAssistenza) {
        this.gestoreAssistenza = gestoreAssistenza;
    }

    // IF-29: L'operatore chiede la lista dei ticket aperti
    @GetMapping("/ticket")
    public ResponseEntity<List<TicketSupporto>> getTicketAperti() {
        return ResponseEntity.ok(gestoreAssistenza.recuperaTicketAperti());
    }

    // IF-24: L'operatore chiude (risolve) un ticket
    @PostMapping("/ticket/{idTicket}/risolvi")
    public ResponseEntity<String> risolviTicket(@PathVariable String idTicket) {
        try {
            gestoreAssistenza.risolviTicket(idTicket);
            return ResponseEntity.ok("Ticket chiuso con successo.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // IF-26: L'operatore blocca un mezzo per manutenzione
    @PostMapping("/mezzo/{idMezzo}/manutenzione")
    public ResponseEntity<String> impostaManutenzione(@PathVariable String idMezzo) {
        try {
            gestoreAssistenza.impostaManutenzioneMezzo(idMezzo);
            return ResponseEntity.ok("Mezzo impostato in manutenzione. Non sarà più noleggiabile.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage()); // Errore: il mezzo è in corsa
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    // IF-27: Endpoint per ottenere la lista di priorità dei veicoli scarichi
    @GetMapping("/mezzi-scarichi")
    public ResponseEntity<List<com.smartmobility.backend.model.Mezzo>> getMezziDaRicaricare() {
        try {
            return ResponseEntity.ok(gestoreAssistenza.recuperaMezziDaRicaricare());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}