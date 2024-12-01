package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.vehicle.Body;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface BodyRepository extends JpaRepository<Body, UUID> {
    @Query("SELECT b FROM Body b WHERE b.body = :body")
    Optional<Body> findByBodyType(@Param("body") String body);
}
