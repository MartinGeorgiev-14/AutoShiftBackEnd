package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.Authentication.AuthResponseDTO;
import com.cars.carSaleWebsite.dto.Authentication.RegisterDto;
import com.cars.carSaleWebsite.dto.Authentication.RoleDto;
import com.cars.carSaleWebsite.dto.Authentication.UserEntityDto;

import javax.management.relation.RoleNotFoundException;
import java.util.HashSet;
import java.util.UUID;

public interface UserEntityService {
    UserEntityDto findUserByUsername(String username);
    Boolean existsEmailByEmail(String email);
    Boolean existsNameByName(String name);
    Boolean isValidPassword(String password);
    String createUser(RegisterDto user) throws RoleNotFoundException;
    AuthResponseDTO getUserInfo();
    HashSet<RoleDto> getUserRoles(UUID id);
}
