package com.smartmobility.backend.business;

import com.smartmobility.backend.model.CodicePromozionale;
import com.smartmobility.backend.repository.CodicePromozionaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GestorePromozioni {

    private final CodicePromozionaleRepository promoRepository;
    private final GestoreWallet gestoreWallet; // Riutilizzo del componente Wallet!

    @Autowired
    public GestorePromozioni(CodicePromozionaleRepository promoRepository, GestoreWallet gestoreWallet) {
        this.promoRepository = promoRepository;
        this.gestoreWallet = gestoreWallet;
    }

    // IF-28: L'operatore genera un codice casuale del valore deciso
    @Transactional
    public String generaCodice(float valore) {
        // Crea un codice alfanumerico es: PROMO-A1B2C3D4
        String codiceGenerato = "PROMO-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        CodicePromozionale nuovoCodice = new CodicePromozionale(codiceGenerato, valore);
        promoRepository.save(nuovoCodice);
        return codiceGenerato;
    }

    // L'utente riscatta il codice
    @Transactional
    public float riscattaCodice(String idUtente, String codice) {
        CodicePromozionale promo = promoRepository.findById(codice)
                .orElseThrow(() -> new RuntimeException("Codice inesistente."));

        if ("RISCATTATO".equals(promo.getStato())) {
            throw new IllegalStateException("Questo codice è già stato utilizzato.");
        }

        // Accredita i soldi sul Wallet dell'utente
        float nuovoSaldo = gestoreWallet.ricaricaWallet(idUtente, promo.getValoreSconto());
        
        // "Brucia" il codice per non renderlo più utilizzabile
        promo.setStato("RISCATTATO");
        promoRepository.save(promo);

        return nuovoSaldo;
    }
}