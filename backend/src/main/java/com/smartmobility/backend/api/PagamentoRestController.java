package com.smartmobility.backend.api;

import com.smartmobility.backend.business.GestorePagamenti;
import com.smartmobility.backend.model.MetodoPagamento;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamenti")
@CrossOrigin(origins = "*")
public class PagamentoRestController {

    private final GestorePagamenti gestorePagamenti;

    // Costruttore pulito senza @Autowired
    public PagamentoRestController(GestorePagamenti gestorePagamenti) {
        this.gestorePagamenti = gestorePagamenti;
    }

    @PostMapping("/{idUtente}/aggiungi")
    public ResponseEntity<String> salvaMetodo(@PathVariable String idUtente, 
                                              @RequestParam String numeroCarta, 
                                              @RequestParam String intestatario, 
                                              @RequestParam String scadenza) {
        try {
            // Controllo validità lunghezza stringa
            if (numeroCarta == null || numeroCarta.length() < 16) {
                return ResponseEntity.badRequest().body("Errore: Il numero della carta deve contenere almeno 16 cifre.");
            }
            
            // IL FIX È QUI: usiamo le variabili passate al metodo, non 'request'
            gestorePagamenti.salvaMetodoPagamento(idUtente, numeroCarta, intestatario, scadenza);
            
            return ResponseEntity.ok("Metodo di pagamento collegato con successo all'account.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{idUtente}/carte")
    public ResponseEntity<List<MetodoPagamento>> visualizzaCarte(@PathVariable String idUtente) {
        try {
            return ResponseEntity.ok(gestorePagamenti.getCarteUtente(idUtente));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}