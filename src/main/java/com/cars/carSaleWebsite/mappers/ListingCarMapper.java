package com.cars.carSaleWebsite.mappers;

import com.cars.carSaleWebsite.dto.ListingCarDto;
import com.cars.carSaleWebsite.dto.ListingImageDto;
import com.cars.carSaleWebsite.dto.UserEntityDto;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.repository.UserEntityRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class ListingCarMapper {

    private UserEntityRepository userEntityRepository;
    private UserEntityMapper userEntityMapper;

    public ListingCarMapper(UserEntityRepository userEntityRepository, UserEntityMapper userEntityMapper) {
        this.userEntityRepository = userEntityRepository;
        this.userEntityMapper = userEntityMapper;
    }

    public ListingCarDto toDTO(ListingVehicle vehicle, UserEntityDto mappedUser, HashSet<ListingImageDto> images){


        ListingCarDto car = new ListingCarDto();

        car.setBody(vehicle.getBody().getBody());
        car.setId(vehicle.getId());
        car.setDesciption(vehicle.getDescription());
        car.setEngine(vehicle.getEngine().getType());
        car.setGearbox(vehicle.getGearbox().getType());
        car.setMake(vehicle.getModel().getMake().getName());
        car.setImages(images);
        car.setHorsepower(vehicle.getHorsepower());
        car.setMileage(vehicle.getMileage());
        car.setModel(vehicle.getModel().getName());
        car.setType(vehicle.getBody().getType().getType());
        car.setPrice(vehicle.getPrice());
        car.setCreatedAt(vehicle.getCreatedAt());
        car.setEditedAt(vehicle.getEditedAt());
        car.setUser(mappedUser);

        return car;

    }
}
