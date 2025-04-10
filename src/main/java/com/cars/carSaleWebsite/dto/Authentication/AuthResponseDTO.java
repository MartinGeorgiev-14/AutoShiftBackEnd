package com.cars.carSaleWebsite.dto.Authentication;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.UUID;

@NoArgsConstructor
@Data
public class AuthResponseDTO {
    private String accessToken;
    private String tokenType = "Bearer ";
    private String username;
    private String fristName;
    private String lastName;
    private String email;
    private String phone;
    private UUID userId;
    private HashSet<RoleDto> roles;

    public AuthResponseDTO(String accessToken, String phone, String email,
                           String fristName, String lastName, String username,
                           UUID userId, HashSet<RoleDto> roles) {
        this.accessToken = accessToken;
        this.phone = phone;
        this.email = email;
        this.fristName = fristName;
        this.lastName = lastName;
        this.username = username;
        this.userId = userId;
        this.roles = roles;
    }
}
