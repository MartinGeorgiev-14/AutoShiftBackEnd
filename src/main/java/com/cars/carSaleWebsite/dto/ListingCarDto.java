package com.cars.carSaleWebsite.dto;

import com.cars.carSaleWebsite.models.entities.listing.ListingImage;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.vehicle.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class ListingCarDto
{
    private UUID id;

    private String make;
    private String model;

    private BigDecimal price;
    private Date createdAt;
    private Date editedAt;
    private int horsepower;
    private int mileage;
    private String desciption;
    private String engine;
    private String gearbox;
    private Set<ListingImageDto> images = new HashSet<>();

    private String type;
    private String body;

    private UserEntityDto user;



}
