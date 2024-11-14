package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.CarDto;
import com.cars.carSaleWebsite.dto.CarPaginationResponse;
import com.cars.carSaleWebsite.exceptions.CarNotFoundException;
import com.cars.carSaleWebsite.models.entities.Car;
import com.cars.carSaleWebsite.models.entities.Motorcycle;
import com.cars.carSaleWebsite.repository.CarRepository;
import com.cars.carSaleWebsite.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private CarRepository carRepository;

    @Autowired
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public CarDto createCar(CarDto carDto) {
        Car car = new Car();
        car.setBrand(carDto.getBrand());
        car.setModel(carDto.getModel());
        car.setColor(carDto.getColor());
        car.setEngineType(carDto.getEngineType());
        car.setMileage(carDto.getMileage());

        Car newCar = carRepository.save(car);

        CarDto carResponse = new CarDto();
        System.out.println(newCar.getId());
        carResponse.setId(newCar.getId());
        carResponse.setBrand(newCar.getBrand());
        carResponse.setModel(newCar.getModel());
        carResponse.setColor(newCar.getColor());
        carResponse.setEngineType(newCar.getEngineType());
        carResponse.setMileage(newCar.getMileage());

        return carResponse;
    }

    @Override
    public List<CarDto> getAllCar() {
        List<Car> car = carRepository.findAll();
        return car.stream().map(c -> mapToDtop(c)).collect(Collectors.toList());
    }

    @Override
    public CarPaginationResponse getByPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Car> cars = carRepository.findAll(pageable);
        List<Car> listOfCar = cars.getContent();
        List<CarDto> content = listOfCar.stream().map(c -> mapToDtop(c)).collect(Collectors.toList());

        CarPaginationResponse carPaginationResponse = new CarPaginationResponse();
        carPaginationResponse.setContent(content);
        carPaginationResponse.setPageNo(cars.getNumber());
        carPaginationResponse.setPageSize(cars.getSize());
        carPaginationResponse.setTotalPages(cars.getTotalPages());
        carPaginationResponse.setTotalElements(cars.getTotalElements());
        carPaginationResponse.setFirst(cars.isFirst());
        carPaginationResponse.setLast(cars.isLast());

        return carPaginationResponse;
    }

    @Override
    public CarDto getCarById(UUID id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car was not found"));
        return mapToDtop(car);
    }

    @Override
    public CarDto updateCar(CarDto carDto, UUID id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car could not be updated"));

        car.setBrand(carDto.getBrand());
        car.setModel(carDto.getModel());
        car.setColor(carDto.getColor());
        car.setMileage(carDto.getMileage());
        car.setEngineType(carDto.getEngineType());

        Car updatedCar = carRepository.save(car);

        return mapToDtop(updatedCar);
    }

    @Override
    public void deleteCar(UUID id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car could not be deleted"));
        carRepository.delete(car);
    }

    private CarDto mapToDtop(Car car){
        CarDto carDto = new CarDto();

        carDto.setId(car.getId());
        carDto.setBrand(car.getBrand());
        carDto.setModel(car.getModel());
        carDto.setEngineType(car.getEngineType());
        carDto.setColor(car.getColor());
        carDto.setMileage(car.getMileage());

        return carDto;
    }

    private Car mapToEntity(CarDto carDto){
        Car car = new Car();

        car.setId(carDto.getId());
        car.setBrand(carDto.getBrand());
        car.setModel(carDto.getModel());
        car.setEngineType(carDto.getEngineType());
        car.setColor(car.getColor());
        car.setMileage(car.getMileage());

        return car;
    }
}
