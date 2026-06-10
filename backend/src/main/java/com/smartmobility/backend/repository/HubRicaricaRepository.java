package com.smartmobility.backend.repository;

import com.smartmobility.backend.model.HubRicarica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HubRicaricaRepository extends JpaRepository<HubRicarica, String> {
}