package com.smartmobility.backend.api;

import com.smartmobility.backend.business.GestoreAccount;
import com.smartmobility.backend.model.Utente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthRestController {

    private final GestoreAccount gestoreAccount;

    // Costruttore per Dependency Injection automatica
    public AuthRestController(GestoreAccount gestoreAccount) {
        this.gestoreAccount = gestoreAccount;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        try {
            Utente utente = gestoreAccount.autenticaUtente(email, password);
            return ResponseEntity.ok(utente);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    // IF-3, IF-18, IF-23: Endpoint di Logout allineato all'UML
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String idSessione) {
        gestoreAccount.disconnettiUtente(idSessione);
        return ResponseEntity.ok("Logout effettuato con successo. Sessione rimossa.");
    }
}