package com.cars.carSaleWebsite.dto.UserFavorites;

import com.cars.carSaleWebsite.dto.Authentication.UserEntityDto;
import com.cars.carSaleWebsite.dto.Listing.ListingImageDto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class FavoriteListingDto {

    private UUID id;

    private String make;
    private String model;

    private BigDecimal price;
    private LocalDate createdAt;
    private LocalDate editedAt;
    private Integer horsepower;
    private Integer mileage;
    private String description;
    private String engine;
    private String gearbox;
    private Integer engineDisplacement;
    private Integer mainImgIndex;
    private UUID mainImgId;
    private List<ListingImageDto> images;
    private String color;
    private String euroStandard;
    private Boolean isActive;
    private LocalDate manufactureDate;
    private String type;
    private String body;
    private String location;
    private String region;

    private UserEntityDto user;

    private Boolean isNotify;
}
