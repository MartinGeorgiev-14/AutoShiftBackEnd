package com.cars.carSaleWebsite.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserEntityDto {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
}
