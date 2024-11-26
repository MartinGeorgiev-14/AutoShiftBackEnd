package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.ListingImageDto;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;

import java.util.HashSet;
import java.util.UUID;

public interface ListingImageService {
    HashSet<ListingImageDto> getAllImagesOfListingById(ListingVehicle id);
}
