package com.smartmobility.backend.repository;

import com.smartmobility.backend.model.ZonaGeofencing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ZonaGeofencingRepository extends JpaRepository<ZonaGeofencing, String> {
}