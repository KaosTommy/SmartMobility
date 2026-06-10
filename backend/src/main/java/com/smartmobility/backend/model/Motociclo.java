package com.smartmobility.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "motocicli")
public class Motociclo extends Mezzo {
    private boolean richiedeCasco = true;
    public Motociclo() {}
    public Motociclo(String idTarga, String idSensore) { super(idTarga, idSensore); }
    public boolean isRichiedeCasco() { return richiedeCasco; }
}