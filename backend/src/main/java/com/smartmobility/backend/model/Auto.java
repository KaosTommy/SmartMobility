package com.smartmobility.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "auto")
public class Auto extends Mezzo {
    private int numeroPosti = 4;
    private String categoriaAmbientale = "Elettrica";
    private String tipoCambio = "Automatico";
    private boolean richiedePatenteB = true;

    public Auto() {}
    public Auto(String idTarga, String idSensore) { super(idTarga, idSensore); }
    
    public boolean isRichiedePatenteB() { return richiedePatenteB; }
    public int getNumeroPosti() { return numeroPosti; }
    public void setNumeroPosti(int numeroPosti) { this.numeroPosti = numeroPosti; }
    public String getCategoriaAmbientale() { return categoriaAmbientale; }
    public void setCategoriaAmbientale(String categoriaAmbientale) { this.categoriaAmbientale = categoriaAmbientale; }
    public String getTipoCambio() { return tipoCambio; }
    public void setTipoCambio(String tipoCambio) { this.tipoCambio = tipoCambio; }
}