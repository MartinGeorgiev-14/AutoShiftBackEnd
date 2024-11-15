package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {
    @Query("SELECT c FROM Car c")
    HashSet<Car> getAll();
}
