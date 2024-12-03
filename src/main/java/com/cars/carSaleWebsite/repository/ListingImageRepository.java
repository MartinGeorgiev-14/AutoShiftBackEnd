package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.listing.ListingImage;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.UUID;

public interface ListingImageRepository extends JpaRepository<ListingImage, UUID> {
    @Query("SELECT lv FROM ListingImage lv WHERE lv.listingId = :id")
    HashSet<ListingImage> getAllListingImagesByListing(@Param("id") ListingVehicle id);

    @Query("SELECT i FROM ListingImage i")
    HashSet<ListingImage> findAllImages();


}

