package com.smartmobility.backend.repository;

import com.smartmobility.backend.model.Corsa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CorsaRepository extends JpaRepository<Corsa, String> {
    // IF-14: Query magica per estrarre tutto lo storico corse di un utente
    List<Corsa> findByUtente_Id(String idUtente);
    List<Corsa> findByUtente(com.smartmobility.backend.model.Utente utente);
}