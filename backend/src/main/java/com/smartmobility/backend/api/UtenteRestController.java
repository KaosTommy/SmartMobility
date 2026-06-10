package com.smartmobility.backend.api;

import com.smartmobility.backend.business.GestoreAccount;
import com.smartmobility.backend.model.Utente;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utenti")
@CrossOrigin(origins = "*")
public class UtenteRestController {

    private final GestoreAccount gestoreAccount;

    // Rimosso @Autowired per pulizia
    public UtenteRestController(GestoreAccount gestoreAccount) {
        this.gestoreAccount = gestoreAccount;
    }

    @PostMapping("/registrati")
    public ResponseEntity<?> registraUtente(@RequestBody Utente utente) {
        try {
            // Ora gestoreAccount.registraUtente restituisce un boolean come da Diagramma delle Classi!
            boolean registrato = gestoreAccount.registraUtente(utente);
            if (registrato) {
                return ResponseEntity.ok(utente);
            } else {
                return ResponseEntity.badRequest().body("Impossibile registrare l'utente.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}