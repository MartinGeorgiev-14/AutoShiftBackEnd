package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.vehicle.Model;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MakeRepository extends JpaRepository<Model, UUID> {

}
