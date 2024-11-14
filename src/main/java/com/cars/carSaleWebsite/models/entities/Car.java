package com.cars.carSaleWebsite.models.entities;

import com.cars.carSaleWebsite.models.abstracts.VehicleAbstract;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Car {

    @Id
    @GeneratedValue
    private UUID id;
    private String brand;
    private String model;
    private String engineType;
    private String color;
    private int mileage;

}
