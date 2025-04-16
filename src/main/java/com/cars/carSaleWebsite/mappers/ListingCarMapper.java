package com.cars.carSaleWebsite.mappers;

import com.cars.carSaleWebsite.dto.Listing.CRUD.CreateCarListingDto;
import com.cars.carSaleWebsite.dto.Listing.CarPaginationResponse;
import com.cars.carSaleWebsite.dto.Listing.ListingCarDto;
import com.cars.carSaleWebsite.dto.Listing.ListingImageDto;
import com.cars.carSaleWebsite.dto.Authentication.UserEntityDto;
import com.cars.carSaleWebsite.models.entities.listing.ListingImage;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.vehicle.*;
import com.cars.carSaleWebsite.repository.UserEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        car.setHorsepower(vehicle.getHorsepower());
        car.setMileage(vehicle.getMileage());
        car.setModel(vehicle.getModel().getName());
        car.setType(vehicle.getBody().getType().getType());
        car.setPrice(vehicle.getPrice());
        car.setCreatedAt(vehicle.getCreatedAt());
        car.setEditedAt(vehicle.getEditedAt());
        car.setEngineDisplacement(vehicle.getEngineDisplacement());
        car.setIsActive(vehicle.getIsActive());
        car.setManufactureDate(vehicle.getManufactureDate());
        car.setColor(vehicle.getColor().getColor());
        car.setEuroStandard(vehicle.getEuroStandard().getStandard());
        car.setUser(mappedUser);
        car.setRegion(vehicle.getLocation().getRegion().getRegion());
        car.setLocation(vehicle.getLocation().getLocation());
        car.setImages(images);

        return car;

    }

    public ListingVehicle toEntityUpdate(ListingVehicle car, ListingCarDto carDto, Model model, Engine engine, Gearbox gearbox, Body body, Location location){

        car.setPrice(carDto.getPrice());
        car.setEditedAt(new Date());
        car.setHorsepower(carDto.getHorsepower());
        car.setMileage(carDto.getMileage());
        car.setDescription(carDto.getDescription());
        car.setEngineDisplacement(carDto.getEngineDisplacement());
        car.setModel(model);
        car.setEngine(engine);
        car.setGearbox(gearbox);
        car.setBody(body);
        car.setLocation(location);

        return car;
    }

    public ListingVehicle toEntity(CreateCarListingDto car, UserEntity user, Model model, Engine engine, Gearbox gearbox, Body body, Location location, Color color, EuroStandard euroStandard){
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
        newCar.setColor(color);
        newCar.setEuroStandard(euroStandard);
        newCar.setIsActive(true);
        newCar.setManufactureDate(car.getManufactureDate());

        return newCar;
    }

    public CarPaginationResponse toPegination(Page<ListingVehicle> listings, List<ListingCarDto> content){
        CarPaginationResponse mapped = new CarPaginationResponse();

        mapped.setContent(content);
        mapped.setPageNo(listings.getNumber());
        mapped.setPageSize(listings.getSize());
        mapped.setTotalPages(listings.getTotalPages());
        mapped.setTotalElements(listings.getTotalElements());
        mapped.setFirst(listings.isFirst());
        mapped.setLast(listings.isLast());

        return mapped;
    }
}
