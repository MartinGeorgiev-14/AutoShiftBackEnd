package com.cars.carSaleWebsite.dto.Listing;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class FilterDto {
    private UUID make;
    private UUID model;
    private UUID userId;
    private UUID region;
    private UUID location;
    private UUID engine;
    private UUID gearbox;
    private UUID type;
    private UUID body;
    private UUID color;
    private UUID euroStandard;
    private Date startYear;
    private Date endYear;
    private BigDecimal startPrice;
    private BigDecimal endPrice;
}
