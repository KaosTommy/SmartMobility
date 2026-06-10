package com.smartmobility.backend.business;

import com.smartmobility.backend.model.Coordinate;
import org.springframework.stereotype.Service;

@Service
public class GestoreFlotta {

    // Centro di Bari vecchio per la simulazione
    private static final double CENTRO_LAT = 41.1171;
    private static final double CENTRO_LON = 16.8718;

    public boolean verificaAreaGeofencing(Coordinate posizione) {
        if (posizione == null) return false;

        // Calcolo matematico elementare della distanza euclidea per la demo
        double distanza = Math.sqrt(Math.pow(posizione.getLatitudine() - CENTRO_LAT, 2) + 
                                    Math.pow(posizione.getLongitudine() - CENTRO_LON, 2));
        
        // Se la distanza dal centro è maggiore di un certo raggio, l'area è interdetta
        return distanza < 0.05; 
    }
}