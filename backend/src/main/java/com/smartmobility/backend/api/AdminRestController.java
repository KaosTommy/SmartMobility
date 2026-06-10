package com.smartmobility.backend.api;

import com.smartmobility.backend.api.dto.DashboardStats;
import com.smartmobility.backend.business.GestoreAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminRestController {

    private final GestoreAdmin gestoreAdmin;

    @Autowired
    public AdminRestController(GestoreAdmin gestoreAdmin) {
        this.gestoreAdmin = gestoreAdmin;
    }

    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getStatistiche() {
        return ResponseEntity.ok(gestoreAdmin.calcolaStatistiche());
    }

    @PostMapping("/utenti/{idUtente}/stato")
    public ResponseEntity<String> modificaStatoUtente(@PathVariable String idUtente, @RequestParam String stato) {
        try {
            gestoreAdmin.cambiaStatoUtente(idUtente, stato.toUpperCase());
            return ResponseEntity.ok("Stato utente aggiornato in: " + stato.toUpperCase());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}