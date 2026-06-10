package com.smartmobility.backend.api;

import com.smartmobility.backend.business.GestorePromozioni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promozioni")
@CrossOrigin(origins = "*")
public class PromozioneRestController {

    private final GestorePromozioni gestorePromozioni;

    @Autowired
    public PromozioneRestController(GestorePromozioni gestorePromozioni) {
        this.gestorePromozioni = gestorePromozioni;
    }

    // IF-28: Operatore chiama questa API per generare un codice
    @PostMapping("/genera")
    public ResponseEntity<String> generaCodice(@RequestParam float importo) {
        try {
            String codice = gestorePromozioni.generaCodice(importo);
            return ResponseEntity.ok("Codice generato con successo: " + codice);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // Utente chiama questa API per riscattarlo
    @PostMapping("/riscatta")
    public ResponseEntity<String> riscattaCodice(@RequestParam String idUtente, @RequestParam String codice) {
        try {
            float nuovoSaldo = gestorePromozioni.riscattaCodice(idUtente, codice);
            return ResponseEntity.ok("Codice riscattato! Il tuo nuovo saldo è: € " + nuovoSaldo);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}