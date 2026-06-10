package com.smartmobility.backend.business;

import com.smartmobility.backend.integration.IPaymentAPI;
import com.smartmobility.backend.model.Corsa;
import com.smartmobility.backend.model.MetodoPagamento;
import com.smartmobility.backend.model.Pagamento;
import com.smartmobility.backend.model.Utente;
import com.smartmobility.backend.repository.CorsaRepository;
import com.smartmobility.backend.repository.MetodoPagamentoRepository;
import com.smartmobility.backend.repository.PagamentoRepository;
import com.smartmobility.backend.repository.UtenteDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GestorePagamenti {

    private final IPaymentAPI paymentAPI;
    private final MetodoPagamentoRepository metodoRepository;
    private final UtenteDAO UtenteDAO;
    private final CorsaRepository corsaRepository;
    // GAP 5: Aggiunto il repository fisico dei pagamenti
    private final PagamentoRepository pagamentoRepository; 

   
    public GestorePagamenti(MetodoPagamentoRepository metodoRepository, UtenteDAO UtenteDAO, 
                            CorsaRepository corsaRepository, PagamentoRepository pagamentoRepository, IPaymentAPI paymentAPI) {
        this.metodoRepository = metodoRepository;
        this.UtenteDAO = UtenteDAO;
        this.corsaRepository = corsaRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.paymentAPI = paymentAPI; 
    }
    
    @Transactional
    public boolean salvaMetodoPagamento(String idUtente, String datiCarta, String intestatario, String scadenza) {
        Utente utente = UtenteDAO.findById(idUtente)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        if (!paymentAPI.validaCarta(datiCarta)) {
    throw new RuntimeException("Carta rifiutata dal Gateway Bancario Esterno");
}
        // Oscuramento carta simulato
        String cartaOscurata = "**** **** **** " + datiCarta.substring(Math.max(0, datiCarta.length() - 4));
        MetodoPagamento nuovoMetodo = new MetodoPagamento("CARD_" + System.currentTimeMillis(), cartaOscurata, intestatario, scadenza, utente);
        metodoRepository.save(nuovoMetodo);
        
        // IF-4 prevede ritorno boolean
        return true; 
    }

    public List<MetodoPagamento> getCarteUtente(String idUtente) {
        Utente utente = UtenteDAO.findById(idUtente).orElseThrow(() -> new RuntimeException("Utente non trovato"));
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
            UtenteDAO.save(utente);
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
        UtenteDAO.save(utente);

        throw new IllegalStateException("Pagamento fallito per mancanza di fondi. Il tuo account è stato SOSPESO.");
    }
}