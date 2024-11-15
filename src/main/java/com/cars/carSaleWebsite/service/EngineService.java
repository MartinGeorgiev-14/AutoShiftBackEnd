package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.CarDto;
import com.cars.carSaleWebsite.dto.EngineDto;

import java.util.HashSet;
import java.util.UUID;

public interface EngineService {
    EngineDto gerEngineById(UUID id);
    HashSet<CarDto> getAllCarsWithTypeIdEngine(UUID engineId);
}
