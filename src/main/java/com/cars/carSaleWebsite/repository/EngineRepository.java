package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.vehicle.Engine;
import com.cars.carSaleWebsite.models.entities.vehicle.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public interface EngineRepository extends JpaRepository<Engine, UUID> {
    Engine getCarEngineById(UUID id);
    @Query("SELECT e from Engine e WHERE e.type = :type")
    Optional<Engine> findEngineByType(@Param("type") String type);

    @Query("SELECT e FROM Engine e")
    HashSet<Engine> getAll();

    @Query("Select m FROM Engine m WHERE m.id = :id")
    Optional<Engine> findByIdOrNull(@Param("id") UUID id);
}
