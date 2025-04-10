package com.cars.carSaleWebsite.dto.Listing.CRUD;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SearchDto {
    private String make;
    private String model;
    private String region;
    private String location;
    private String engine;
    private String gearbox;
    private String body;
    private Date startYear;
    private Date endYear;
    private BigDecimal startPrice;
    private BigDecimal endPrice;
}
