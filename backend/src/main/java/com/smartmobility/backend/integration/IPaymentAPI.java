package com.smartmobility.backend.integration;

public interface IPaymentAPI {
    boolean validaCarta(String datiCarta);
    String processaTransazione(String tokenPagamento, float importo);
    boolean eseguiStorno(String idTransazione, float importo); // Forza tipo float da UML
}