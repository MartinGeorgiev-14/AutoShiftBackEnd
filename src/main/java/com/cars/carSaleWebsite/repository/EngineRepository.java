package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.vehicle.Engine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EngineRepository extends JpaRepository<Engine, UUID> {
    Engine getCarEngineById(UUID id);

}
