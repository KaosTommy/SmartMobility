package com.smartmobility.backend.repository;

import com.smartmobility.backend.model.Mezzo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MezzoRepository extends JpaRepository<Mezzo, String> {
    
    // IF-27: Trova tutti i mezzi con batteria inferiore a una certa soglia
    List<Mezzo> findByLivelloBatteriaLessThan(int livelloBatteria);
}