package com.cars.carSaleWebsite.dto.Listing;

import lombok.Data;

@Data
public class ListingImageDto {
    private boolean isMain;
    private String type;
    private String url;
}
