package com.cars.carSaleWebsite.dto;

import lombok.Data;

@Data
public class ListingImageDto {
    private boolean isMain;
    private String type;
    private byte[] imageData;
}
