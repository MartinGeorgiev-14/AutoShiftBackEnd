package com.cars.carSaleWebsite.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CarDto
{
    private UUID id;
    private String brand;
    private String model;
    private String engineType;
    private String color;
    private int mileage;
}
