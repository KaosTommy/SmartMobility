package com.smartmobility.backend.repository;

import com.smartmobility.backend.model.Multa;
import com.smartmobility.backend.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MultaRepository extends JpaRepository<Multa, String> {
    List<Multa> findByUtente(Utente utente);
}