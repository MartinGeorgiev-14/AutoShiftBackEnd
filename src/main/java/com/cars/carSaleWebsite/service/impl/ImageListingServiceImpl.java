package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.ListingImageDto;
import com.cars.carSaleWebsite.models.entities.listing.ListingImage;
import com.cars.carSaleWebsite.repository.ListingImageRepository;
import com.cars.carSaleWebsite.service.ListingImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.UUID;

@Service
public class ImageListingServiceImpl implements ListingImageService {

    ListingImageRepository listingImageRepository;

    @Autowired
    public ImageListingServiceImpl(ListingImageRepository listingImageRepository) {
        this.listingImageRepository = listingImageRepository;
    }

    @Override
    public HashSet<ListingImageDto> getAllImagesOfListing(UUID id) {
        Set<ListingImage> images = listingImageRepository.getAllListingImagesByListing(id);
        HashSet<ListingImageDto> mapped = (HashSet<ListingImageDto>) images.stream().map(i -> mapToDto(i)).collect(Collectors.toSet());

        return mapped;
    }

    private ListingImageDto mapToDto(ListingImage list){
        ListingImageDto newItem = new ListingImageDto();

        newItem.setImageData(list.getImageData());
        newItem.setType(list.getType());
        newItem.setMain(list.getIsMain());

        return newItem;
    }
}
