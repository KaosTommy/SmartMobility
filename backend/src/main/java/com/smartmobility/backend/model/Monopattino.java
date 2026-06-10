package com.smartmobility.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "monopattini")
public class Monopattino extends Mezzo {
    public Monopattino() {}
    public Monopattino(String idTarga, String idSensore) { super(idTarga, idSensore); }
}