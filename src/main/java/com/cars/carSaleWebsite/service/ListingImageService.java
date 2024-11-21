package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.ListingImageDto;

import java.util.HashSet;
import java.util.UUID;

public interface ListingImageService {
    HashSet<ListingImageDto> getAllImagesOfListing(UUID id);
}
