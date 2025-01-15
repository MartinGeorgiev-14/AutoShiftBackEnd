package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.vehicle.Make;
import com.cars.carSaleWebsite.models.entities.vehicle.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashSet;
import java.util.UUID;

public interface MakeRepository extends JpaRepository<Make, UUID> {
    @Query("SELECT m FROM Make m")
    HashSet<Make> getAll();
}
