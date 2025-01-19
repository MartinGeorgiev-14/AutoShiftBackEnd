package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.AuthResponseDTO;
import com.cars.carSaleWebsite.dto.RegisterDto;
import com.cars.carSaleWebsite.dto.UserEntityDto;

import javax.management.relation.RoleNotFoundException;

public interface UserEntityService {
    UserEntityDto findUserByUsername(String username);
    Boolean existsEmailByEmail(String email);
    Boolean existsNameByName(String name);
    Boolean isValidPassword(String password);
    String createUser(RegisterDto user) throws RoleNotFoundException;
    AuthResponseDTO getUserInfo();
}
