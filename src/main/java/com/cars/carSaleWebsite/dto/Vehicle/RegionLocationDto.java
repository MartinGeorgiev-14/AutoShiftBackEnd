package com.cars.carSaleWebsite.dto.Vehicle;

import lombok.Data;

import java.util.UUID;

@Data
public class RegionLocationDto {
    private UUID regionId;
    private String region;

    private UUID locationId;
    private String location;
}
