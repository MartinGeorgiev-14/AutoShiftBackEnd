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
    private Date dateStart;
    private Date dateEnd;
    private BigDecimal priceStart;
    private BigDecimal priceEnd;
    private Integer horsepowerStart;
    private Integer horsepowerEnd;
    private Integer mileageStart;
    private Integer mileageEnd;
    private Integer engineDisplacementStart;
    private Integer engineDisplacementEnd;
}
