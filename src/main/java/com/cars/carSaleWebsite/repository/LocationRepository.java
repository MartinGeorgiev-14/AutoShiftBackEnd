package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.vehicle.Location;
import com.cars.carSaleWebsite.models.entities.vehicle.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, UUID> {
    @Query("SELECT l FROM Location l WHERE l.location = :location")
    Optional<Location> findByRegion(@Param("location") String location);

    @Query("SELECT l FROM Location l")
    HashSet<Location> getAll();
}
