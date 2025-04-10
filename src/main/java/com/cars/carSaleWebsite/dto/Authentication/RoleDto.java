package com.cars.carSaleWebsite.dto.Authentication;

import lombok.Data;

import java.util.UUID;

@Data
public class RoleDto {
    private UUID id;
    private String name;
}
