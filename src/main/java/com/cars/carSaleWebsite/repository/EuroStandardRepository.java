package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.vehicle.Color;
import com.cars.carSaleWebsite.models.entities.vehicle.EuroStandard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashSet;
import java.util.UUID;

public interface EuroStandardRepository extends JpaRepository<EuroStandard, UUID> {
    @Query("SELECT e FROM EuroStandard e")
    HashSet<EuroStandard> getAll();
}
