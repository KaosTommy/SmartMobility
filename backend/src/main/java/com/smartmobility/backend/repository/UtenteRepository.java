package com.smartmobility.backend.repository;

import com.smartmobility.backend.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, String> {
    
    // Metodo utilizzato da GestoreAccount per la Registrazione
    Optional<Utente> findByEmail(String email);

    // Metodo utilizzato da AuthRestController per il Login
    Optional<Utente> findByEmailAndPasswordHash(String email, String passwordHash);
}