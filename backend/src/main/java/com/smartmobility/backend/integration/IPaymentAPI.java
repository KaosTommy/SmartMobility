package com.smartmobility.backend.integration;

import org.springframework.stereotype.Service;

public interface IPaymentAPI {
    boolean validaCarta(String datiCarta);
    String processaTransazione(String tokenPagamento, float importo);
}

@Service
class PaymentAPIMock implements IPaymentAPI {
    @Override public boolean validaCarta(String datiCarta) { return true; }
    @Override public String processaTransazione(String token, float imp) { return "BANK_RECEIPT_" + System.currentTimeMillis(); }
}