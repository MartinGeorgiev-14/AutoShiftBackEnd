package com.cars.carSaleWebsite.mappers;

import com.cars.carSaleWebsite.dto.ListingCarDto;
import com.cars.carSaleWebsite.dto.ListingImageDto;
import com.cars.carSaleWebsite.dto.UserEntityDto;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.vehicle.*;
import com.cars.carSaleWebsite.repository.UserEntityRepository;
import com.cars.carSaleWebsite.service.ListingCarService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Component
public class ListingCarMapper {

    private UserEntityRepository userEntityRepository;
    private UserEntityMapper userEntityMapper;

    public ListingCarMapper(UserEntityRepository userEntityRepository, UserEntityMapper userEntityMapper) {
        this.userEntityRepository = userEntityRepository;
        this.userEntityMapper = userEntityMapper;
    }

    public ListingCarDto toDTO(ListingVehicle vehicle, UserEntityDto mappedUser, List<ListingImageDto> images){


        ListingCarDto car = new ListingCarDto();

        car.setBody(vehicle.getBody().getBody());
        car.setId(vehicle.getId());
        car.setDescription(vehicle.getDescription());
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
        car.setRegion(vehicle.getLocation().getRegion().getRegion());
        car.setLocation(vehicle.getLocation().getLocation());

        return car;

    }

    public ListingVehicle toEntity(ListingCarDto car, UserEntity user, Model model, Engine engine, Gearbox gearbox, Body body, Location location){
        ListingVehicle newCar = new ListingVehicle();

        newCar.setPrice(car.getPrice());
        newCar.setCreatedAt(new Date());
        newCar.setHorsepower(car.getHorsepower());
        newCar.setMileage(car.getMileage());
        newCar.setDescription(car.getDescription());
        newCar.setEngineDisplacement(car.getEngineDisplacement());
        newCar.setModel(model);
        newCar.setEngine(engine);
        newCar.setGearbox(gearbox);
        newCar.setBody(body);
        newCar.setUserEntity(user);
        newCar.setLocation(location);

        return newCar;
    }
}
