package com.cars.carSaleWebsite.dto.Vehicle;

import lombok.Data;

import java.util.UUID;

@Data
public class BrandModelDto {
    private UUID brandId;
    private String brand;

    private UUID modelId;
    private String model;
}
