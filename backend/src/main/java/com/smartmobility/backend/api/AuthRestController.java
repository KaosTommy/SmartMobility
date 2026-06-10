package com.smartmobility.backend.api;

import com.smartmobility.backend.model.Utente;
import com.smartmobility.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthRestController {

    @Autowired
    private UtenteRepository utenteRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        Optional<Utente> utente = utenteRepository.findByEmailAndPasswordHash(email, password);
        
        if (utente.isPresent()) {
            Utente u = utente.get();
            // Controllo sicurezza (IF-12 e IF-16)
            if ("SOSPESO".equals(u.getStatoAccount()) || "ELIMINATO".equals(u.getStatoAccount())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accesso negato: Account Sospeso o Eliminato.");
            }
            return ResponseEntity.ok(u);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali non valide.");
        }
    }
}