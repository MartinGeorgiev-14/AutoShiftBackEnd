package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.vehicle.Color;
import com.cars.carSaleWebsite.models.entities.vehicle.Engine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashSet;
import java.util.UUID;

public interface ColorRepository extends JpaRepository<Color, UUID> {
    @Query("SELECT c FROM Color c")
    HashSet<Color> getAll();
}
