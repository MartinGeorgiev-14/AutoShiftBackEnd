package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.vehicle.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public interface ModelRepository extends JpaRepository<Model, UUID> {

    @Query("Select m from Model m where m.name = :name")
    Optional<Model> findByModelName(@Param("name") String name);

    @Query("SELECT m FROM Model m")
    HashSet<Model> getALl();
}
