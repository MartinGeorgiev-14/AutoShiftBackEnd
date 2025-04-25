package com.cars.carSaleWebsite.dto.Vehicle;

import lombok.Data;

import java.util.UUID;

@Data
public class EngineDto {
    private UUID id;
    private String engine;
}
