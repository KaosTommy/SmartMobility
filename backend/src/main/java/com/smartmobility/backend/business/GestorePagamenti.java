package com.smartmobility.backend.business;

import com.smartmobility.backend.model.Corsa;
import com.smartmobility.backend.model.MetodoPagamento;
import com.smartmobility.backend.model.Pagamento;
import com.smartmobility.backend.model.Utente;
import com.smartmobility.backend.repository.CorsaRepository;
import com.smartmobility.backend.repository.MetodoPagamentoRepository;
import com.smartmobility.backend.repository.PagamentoRepository;
import com.smartmobility.backend.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GestorePagamenti {

    private final MetodoPagamentoRepository metodoRepository;
    private final UtenteRepository utenteRepository;
    private final CorsaRepository corsaRepository;
    // GAP 5: Aggiunto il repository fisico dei pagamenti
    private final PagamentoRepository pagamentoRepository; 

    @Autowired
    public GestorePagamenti(MetodoPagamentoRepository metodoRepository, UtenteRepository utenteRepository, 
                            CorsaRepository corsaRepository, PagamentoRepository pagamentoRepository) {
        this.metodoRepository = metodoRepository;
        this.utenteRepository = utenteRepository;
        this.corsaRepository = corsaRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    @Transactional
    public void aggiungiCarta(String idUtente, String numeroCarta, String intestatario, String scadenza) {
        Utente utente = utenteRepository.findById(idUtente)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        
        String cartaOscurata = "**** **** **** " + numeroCarta.substring(numeroCarta.length() - 4);
        MetodoPagamento nuovoMetodo = new MetodoPagamento("CARD_" + System.currentTimeMillis(), cartaOscurata, intestatario, scadenza, utente);
        metodoRepository.save(nuovoMetodo);
    }

    public List<MetodoPagamento> getCarteUtente(String idUtente) {
        Utente utente = utenteRepository.findById(idUtente).orElseThrow(() -> new RuntimeException("Utente non trovato"));
        return metodoRepository.findByUtente(utente);
    }

    @Transactional
    public boolean elaboraAddebito(String idCorsa, float importo) {
        Corsa corsa = corsaRepository.findById(idCorsa).orElseThrow(() -> new RuntimeException("Corsa non trovata"));
        Utente utente = corsa.getUtente();

        // Creazione dell'entità Pagamento (per lo storico aziendale)
        Pagamento nuovoPagamento = new Pagamento("PAG_" + System.currentTimeMillis(), importo, corsa);

        // 1. Proviamo col Wallet
        if (utente.getSaldoWallet() >= importo) {
            utente.aggiungiFondi(-importo);
            utenteRepository.save(utente);
            nuovoPagamento.setStato("SALDATO");
            pagamentoRepository.save(nuovoPagamento);
            return true;
        }

        // 2. Proviamo con la Carta
        List<MetodoPagamento> carte = metodoRepository.findByUtente(utente);
        if (!carte.isEmpty()) {
            nuovoPagamento.setStato("SALDATO");
            pagamentoRepository.save(nuovoPagamento);
            return true;
        }

        // CORREZIONE GAP 3: Gestione dell'Insolvenza
        // Il pagamento fallisce, viene segnato come insolvente e l'utente viene sospeso.
        nuovoPagamento.setStato("INSOLVENTE");
        pagamentoRepository.save(nuovoPagamento);

        utente.setStatoAccount("SOSPESO");
        utenteRepository.save(utente);

        throw new IllegalStateException("Pagamento fallito per mancanza di fondi. Il tuo account è stato SOSPESO.");
    }
}