package com.cars.carSaleWebsite.mappers;

import com.cars.carSaleWebsite.dto.Authentication.UserEntityDto;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {
    public UserEntityDto toDTO(UserEntity user){
        UserEntityDto mapped = new UserEntityDto();

        mapped.setPhone(user.getPhone());
        mapped.setId(user.getId());
        mapped.setEmail(user.getEmail());
        mapped.setUsername(user.getUsername());
        mapped.setLastName(user.getLastName());
        mapped.setFirstName(user.getFirstName());

        return mapped;
    }
}
