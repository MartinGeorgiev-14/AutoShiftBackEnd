package com.cars.carSaleWebsite.dto.Authentication;

import lombok.Data;

@Data
public class RegisterDto {

    private String message;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;

}
