package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.Listing.ListingImageDto;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;

import java.util.List;

public interface ListingImageService {
    List<ListingImageDto> getAllImagesOfListingById(ListingVehicle id);
}
