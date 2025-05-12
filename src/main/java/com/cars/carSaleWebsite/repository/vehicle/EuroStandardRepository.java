package com.cars.carSaleWebsite.repository.vehicle;

import com.cars.carSaleWebsite.models.entities.vehicle.EuroStandard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

public interface EuroStandardRepository extends JpaRepository<EuroStandard, UUID> {
    @Query("SELECT e FROM EuroStandard e")
    HashSet<EuroStandard> getAll();

    @Query("Select e FROM EuroStandard e WHERE e.id = :id")
    Optional<EuroStandard> findByIdOrNull(@Param("id") UUID id);
}
