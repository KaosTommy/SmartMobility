package com.smartmobility.backend.api;

import com.smartmobility.backend.api.dto.*;
import com.smartmobility.backend.business.*;
import com.smartmobility.backend.model.Ricevuta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/corse")
@CrossOrigin(origins = "*") // Permette al file HTML locale di comunicare con il server
public class CorsaRestController {

    private final GestoreNoleggio gestoreNoleggio;
    private final GestorePagamenti gestorePagamenti;

    @Autowired
    public CorsaRestController(GestoreNoleggio gestoreNoleggio, GestorePagamenti gestorePagamenti) {
        this.gestoreNoleggio = gestoreNoleggio;
        this.gestorePagamenti = gestorePagamenti;
    }

    // IF-8: Sblocca e avvia
   @PostMapping("/avvia")
    public ResponseEntity<String> avviaCorsa(@RequestBody AzioneMezzoRequest request) {
        try {
            String idCorsa = gestoreNoleggio.avviaCorsa(request.idUtente(), request.idMezzo());
            return ResponseEntity.ok(idCorsa); // <-- MODIFICA: Inviamo l'ID al frontend
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore di sistema: " + e.getMessage());
        }
    }

    // IF-10: Termina corsa
    @PostMapping("/{idCorsa}/termina")
    public ResponseEntity<?> terminaCorsa(@PathVariable String idCorsa) {
        try {
            Ricevuta ricevuta = gestoreNoleggio.terminaCorsa(idCorsa);
            return ResponseEntity.ok(ricevuta); // Restituisce il JSON per il checkout
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage()); // Errore Geofencing
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore hardware.");
        }
    }

    // IF-12: Paga il saldo
    @PostMapping("/{idCorsa}/paga")
    public ResponseEntity<String> pagaCorsa(@PathVariable String idCorsa, @RequestBody PagamentoRequest request) {
        boolean successo = gestorePagamenti.elaboraAddebito(idCorsa, request.importo());
        if (successo) {
            return ResponseEntity.ok("Pagamento elaborato correttamente dal Gateway Bancario.");
        }
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body("Transazione fallita o fondi insufficienti.");
    }
    // IF-7: Prenota mezzo
    @PostMapping("/prenota")
    public ResponseEntity<String> prenotaMezzo(@RequestBody AzioneMezzoRequest request) {
        try {
            gestoreNoleggio.prenotaMezzo(request.idUtente(), request.idMezzo());
            return ResponseEntity.ok("Mezzo prenotato con successo! Hai 15 minuti per raggiungerlo e sbloccarlo.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore di sistema: " + e.getMessage());
        }
    }
    // IF-11: Endpoint per mettere in pausa
    @PostMapping("/{idCorsa}/pausa")
    public ResponseEntity<String> pausaCorsa(@PathVariable String idCorsa) {
        try {
            gestoreNoleggio.mettiInPausaCorsa(idCorsa);
            return ResponseEntity.ok("Corsa in pausa. Il motore è bloccato.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // IF-11: Endpoint per riprendere la marcia
    @PostMapping("/{idCorsa}/riprendi")
    public ResponseEntity<String> riprendiCorsa(@PathVariable String idCorsa) {
        try {
            gestoreNoleggio.riprendiCorsaInPausa(idCorsa);
            return ResponseEntity.ok("Corsa ripresa. Buon viaggio!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}