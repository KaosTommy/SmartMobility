package com.smartmobility.backend.api;

import com.smartmobility.backend.model.HubRicarica;
import com.smartmobility.backend.repository.HubRicaricaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mappa")
@CrossOrigin(origins = "*")
public class MappaRestController {

    private final HubRicaricaRepository hubRepository;

    @Autowired
    public MappaRestController(HubRicaricaRepository hubRepository) {
        this.hubRepository = hubRepository;
    }

    // IF-15: Il frontend chiede la lista delle stazioni di ricarica da disegnare
    @GetMapping("/hubs")
    public ResponseEntity<List<HubRicarica>> getStazioniDiRicarica() {
        return ResponseEntity.ok(hubRepository.findAll());
    }
}