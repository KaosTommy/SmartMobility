package com.smartmobility.backend.api;

import com.smartmobility.backend.model.StazioneRicarica;
import com.smartmobility.backend.repository.StazioneRicaricaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mappa")
@CrossOrigin(origins = "*")
public class MappaRestController {

    private final StazioneRicaricaRepository stazioneRepository;

    public MappaRestController(StazioneRicaricaRepository stazioneRepository) {
        this.stazioneRepository = stazioneRepository;
    }

    @GetMapping("/hubs")
    public ResponseEntity<List<StazioneRicarica>> getStazioni() {
        return ResponseEntity.ok(stazioneRepository.findAll());
    }
}