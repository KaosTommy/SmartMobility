package com.smartmobility.backend.integration;

import com.smartmobility.backend.model.Coordinate;
import org.springframework.stereotype.Service;

public interface ISensorAPI {
    Coordinate rilevaPosizione(String idSensore);
    boolean inviaComandoSblocco(String idSensore);
    boolean inviaComandoBlocco(String idSensore);
}

@Service
class SensorAPIMock implements ISensorAPI {
    @Override public Coordinate rilevaPosizione(String idSensore) { return new Coordinate(41.1171, 16.8718); }
    @Override public boolean inviaComandoSblocco(String idSensore) { return true; }
    @Override public boolean inviaComandoBlocco(String idSensore) { return true; }
}