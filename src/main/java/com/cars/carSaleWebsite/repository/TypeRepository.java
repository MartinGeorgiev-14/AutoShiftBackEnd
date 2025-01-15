package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.vehicle.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashSet;
import java.util.UUID;

public interface TypeRepository extends JpaRepository<Type, UUID> {
    @Query("SELECT t FROM Type t")
    HashSet<Type> getAll();
}
