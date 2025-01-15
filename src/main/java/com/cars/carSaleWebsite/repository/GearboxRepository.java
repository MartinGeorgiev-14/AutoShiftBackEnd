package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.vehicle.Gearbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public interface GearboxRepository extends JpaRepository<Gearbox, UUID> {

    @Query("SELECT g from Gearbox g WHERE g.type = :type")
    Optional<Gearbox> findGearboxByType(@Param("type") String type);

    @Query("SELECT g FROM Gearbox g")
    HashSet<Gearbox> getAll();
}
