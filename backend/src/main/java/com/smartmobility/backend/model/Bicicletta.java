package com.smartmobility.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "biciclette")
public class Bicicletta extends Mezzo {
    private boolean isElettrica = true;
    private boolean haCestino = true;

    public Bicicletta() {}
    public Bicicletta(String idTarga, String idSensore) { super(idTarga, idSensore); }

    public boolean isElettrica() { return isElettrica; }
    public void setElettrica(boolean isElettrica) { this.isElettrica = isElettrica; }
    public boolean isHaCestino() { return haCestino; }
    public void setHaCestino(boolean haCestino) { this.haCestino = haCestino; }
}