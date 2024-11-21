package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.listing.ListingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public interface ListingImageRepository extends JpaRepository<ListingImage, UUID> {
    @Query("SELECT i FROM ListingImage i WHERE i.listingId = :id")
    Set<ListingImage> getAllListingImagesByListing(@Param("id") UUID id);
}
