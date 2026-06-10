package com.smartmobility.backend.api;

import com.smartmobility.backend.business.GestoreWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin(origins = "*")
public class WalletRestController {

    private final GestoreWallet gestoreWallet;

    @Autowired
    public WalletRestController(GestoreWallet gestoreWallet) {
        this.gestoreWallet = gestoreWallet;
    }

    @GetMapping("/{idUtente}/saldo")
    public ResponseEntity<Float> getSaldo(@PathVariable String idUtente) {
        try {
            return ResponseEntity.ok(gestoreWallet.getSaldo(idUtente));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0.0f);
        }
    }

    @PostMapping("/{idUtente}/ricarica")
    public ResponseEntity<?> ricaricaSaldo(@PathVariable String idUtente, @RequestParam float importo) {
        try {
            float nuovoSaldo = gestoreWallet.ricaricaWallet(idUtente, importo);
            return ResponseEntity.ok(nuovoSaldo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore di ricarica.");
        }
    }
}