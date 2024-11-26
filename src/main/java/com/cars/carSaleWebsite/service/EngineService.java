package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.ListingCarDto;
import com.cars.carSaleWebsite.dto.EngineDto;

import java.util.HashSet;
import java.util.UUID;

public interface EngineService {
    EngineDto gerEngineById(UUID id);
    HashSet<ListingCarDto> getAllCarsWithTypeIdEngine(UUID engineId);
}
