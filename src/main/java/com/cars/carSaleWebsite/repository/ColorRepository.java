package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.vehicle.Color;
import com.cars.carSaleWebsite.models.entities.vehicle.Engine;
import com.cars.carSaleWebsite.models.entities.vehicle.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public interface ColorRepository extends JpaRepository<Color, UUID> {
    @Query("SELECT c FROM Color c")
    HashSet<Color> getAll();

    @Query("Select c FROM Color c WHERE c.id = :id")
    Optional<Color> findByIdOrNull(@Param("id") UUID id);
}
