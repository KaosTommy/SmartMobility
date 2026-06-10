package com.smartmobility.backend.integration;

import org.springframework.stereotype.Component;

@Component
public class PaymentAPIMock implements IPaymentAPI {

    @Override
    public boolean validaCarta(String datiCarta) {
        return datiCarta != null && datiCarta.length() >= 16;
    }

    @Override
    public String processaTransazione(String tokenPagamento, float importo) {
        return "TXN_OK_" + System.currentTimeMillis();
    }

    @Override
    public boolean eseguiStorno(String idTransazione, float importo) {
        System.out.println("Storno bancario eseguito per transazione: " + idTransazione + " - Importo: €" + importo);
        return true;
    }
}