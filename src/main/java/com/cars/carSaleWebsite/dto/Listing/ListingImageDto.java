package com.cars.carSaleWebsite.dto.Listing;

import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import lombok.Data;

import java.util.UUID;

@Data
public class ListingImageDto {
    private boolean isMain;
    private String type;
    private String url;
    private String publicId;
    private UUID listingId;
}
