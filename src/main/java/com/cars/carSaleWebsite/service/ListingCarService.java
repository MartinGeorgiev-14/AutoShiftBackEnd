package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.ListingCarDto;

import java.util.UUID;

public interface ListingCarService {
    ListingCarDto getCarById(UUID id);
}
