package com.smartmobility.backend.repository;

import com.smartmobility.backend.model.StazioneRicarica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StazioneRicaricaRepository extends JpaRepository<StazioneRicarica, String> {
}