package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.vehicle.Gearbox;
import com.cars.carSaleWebsite.models.entities.vehicle.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public interface TypeRepository extends JpaRepository<Type, UUID> {
    @Query("SELECT t FROM Type t")
    HashSet<Type> getAll();

    @Query("Select m FROM Type m WHERE m.id = :id")
    Optional<Type> findByIdOrNull(@Param("id") UUID id);
}
