package com.smartmobility.backend.repository;

import com.smartmobility.backend.model.TicketSupporto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketSupportoRepository extends JpaRepository<TicketSupporto, String> {
    // Spring Boot scriverà la query SQL in automatico per trovare solo i ticket aperti!
    List<TicketSupporto> findByStato(String stato);
}