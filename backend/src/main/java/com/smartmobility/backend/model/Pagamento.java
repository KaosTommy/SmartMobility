package com.smartmobility.backend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "pagamenti")
public class Pagamento {

    @Id
    private String idPagamento;
    private float importo;
    private String stato; // "SALDATO", "INSOLVENTE"
    private Timestamp dataPagamento;

    @OneToOne
    @JoinColumn(name = "id_corsa")
    private Corsa corsa;

    public Pagamento() {}

    public Pagamento(String idPagamento, float importo, Corsa corsa) {
        this.idPagamento = idPagamento;
        this.importo = importo;
        this.corsa = corsa;
        this.stato = "IN ELABORAZIONE";
        this.dataPagamento = new Timestamp(System.currentTimeMillis());
    }

    public String getIdPagamento() { return idPagamento; }
    public float getImporto() { return importo; }
    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
    public Corsa getCorsa() { return corsa; }
public java.sql.Timestamp getDataPagamento() { return dataPagamento; }
}