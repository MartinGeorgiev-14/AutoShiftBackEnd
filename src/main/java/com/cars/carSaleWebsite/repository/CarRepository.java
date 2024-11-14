package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID> {

}
