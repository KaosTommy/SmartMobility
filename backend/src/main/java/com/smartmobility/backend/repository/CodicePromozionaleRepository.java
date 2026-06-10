package com.smartmobility.backend.repository;

import com.smartmobility.backend.model.CodicePromozionale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodicePromozionaleRepository extends JpaRepository<CodicePromozionale, String> {
}