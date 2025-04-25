package com.cars.carSaleWebsite.dto.Vehicle;

import lombok.Data;

import java.util.UUID;

@Data
public class TypeBodyDto {
    private UUID typeId;
    private String type;

    private UUID bodyId;
    private String body;
}
