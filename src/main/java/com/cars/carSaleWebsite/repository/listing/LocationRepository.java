package com.cars.carSaleWebsite.repository.listing;

import com.cars.carSaleWebsite.models.entities.vehicle.Location;
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

    @Query("Select m FROM Location m WHERE m.id = :id")
    Optional<Location> findByIdOrNull(@Param("id") UUID id);
}
