package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.vehicle.Model;
import com.cars.carSaleWebsite.models.entities.vehicle.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface RegionRepository extends JpaRepository<Region, UUID> {
    @Query("SELECT r FROM Region r")
    HashSet<Region> getAll();

    @Query("Select m FROM Region m WHERE m.id = :id")
    Optional<Region> findByIdOrNull(@Param("id") UUID id);
}
