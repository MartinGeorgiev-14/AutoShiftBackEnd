package com.cars.carSaleWebsite.dto.Listing.CRUD;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class FilterDto {
    private UUID make;
    private String name;
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
    private LocalDate manufactureDateStart;
    private LocalDate manufactureDateEnd;
    private BigDecimal priceStart;
    private BigDecimal priceEnd;
    private Integer horsepowerStart;
    private Integer horsepowerEnd;
    private Integer mileageStart;
    private Integer mileageEnd;
    private Integer engineDisplacementStart;
    private Integer engineDisplacementEnd;
    private LocalDate createdStart;
    private LocalDate createdEnd;
}
