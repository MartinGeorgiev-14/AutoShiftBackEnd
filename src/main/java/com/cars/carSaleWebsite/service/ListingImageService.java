package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.ListingImageDto;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public interface ListingImageService {
    List<ListingImageDto> getAllImagesOfListingById(ListingVehicle id);
}
