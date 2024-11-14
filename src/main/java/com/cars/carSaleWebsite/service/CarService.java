package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.CarDto;
import com.cars.carSaleWebsite.dto.CarPaginationResponse;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public interface CarService {
    CarDto createCar(CarDto carDto);
    List<CarDto> getAllCar();
    CarPaginationResponse getByPage(int pageNo, int pageSize);
    CarDto getCarById(UUID id);
    CarDto updateCar(CarDto carDto, UUID id);
    void deleteCar(UUID id);

}
