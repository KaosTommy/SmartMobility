package com.smartmobility.backend.repository;

import com.smartmobility.backend.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtenteDAO extends JpaRepository<Utente, String> {
    
    Optional<Utente> findByEmail(String email);

    // TRUCCO PER RISPETTARE IL DIAGRAMMA DI SEQUENZA AL 100%
    // Espone il metodo "insert" che fa da proxy al metodo "save" di JPA
    default boolean insert(Utente utente) {
        this.save(utente);
        return true;
    }
}