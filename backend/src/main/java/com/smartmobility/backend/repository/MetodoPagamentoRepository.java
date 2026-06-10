package com.smartmobility.backend.repository;

import com.smartmobility.backend.model.MetodoPagamento;
import com.smartmobility.backend.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MetodoPagamentoRepository extends JpaRepository<MetodoPagamento, String> {
    List<MetodoPagamento> findByUtente(Utente utente);
}