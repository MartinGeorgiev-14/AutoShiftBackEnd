package com.cars.carSaleWebsite.dto.Listing.CRUD;

import com.cars.carSaleWebsite.dto.Listing.ListingImageDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class CreateCarListingDto {
//    private UUID make;
    private UUID model;

    private BigDecimal price;
    private Date createdAt;
    private Date editedAt;
    private Integer horsepower;
    private Integer mileage;
    private String description;
    private UUID engine;
    private UUID gearbox;
    private Integer engineDisplacement;
    private Integer mainImgIndex;
    private UUID mainImgId;
    private List<ListingImageDto> images;

//    private String type;
    private UUID body;
    private UUID location;
//    private String region;

}
