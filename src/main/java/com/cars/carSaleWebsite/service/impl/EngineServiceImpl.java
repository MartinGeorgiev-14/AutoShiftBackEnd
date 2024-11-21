//package com.cars.carSaleWebsite.service.impl;
//
//import com.cars.carSaleWebsite.dto.CarDto;
//import com.cars.carSaleWebsite.dto.EngineDto;
//import com.cars.carSaleWebsite.exceptions.EngineNotFoundException;
//import com.cars.carSaleWebsite.models.entities.listing.ListingCar;
//import com.cars.carSaleWebsite.models.entities.vehicle.Engine;
//import com.cars.carSaleWebsite.repository.ListingCarRepository;
//import com.cars.carSaleWebsite.repository.EngineRepository;
//import com.cars.carSaleWebsite.service.EngineService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//public class EngineServiceImpl implements EngineService {
//
//    private EngineRepository engineRepository;
//    private ListingCarRepository listingCarRepository;
//
//    @Autowired
//    EngineServiceImpl(EngineRepository engineRepository, ListingCarRepository listingCarRepository){
//        this.engineRepository = engineRepository;
//        this.listingCarRepository = listingCarRepository;
//    }
//
//    @Override
//    public EngineDto gerEngineById(UUID id) {
//
//        Engine engine = engineRepository.findById(id).orElseThrow(() -> new EngineNotFoundException("Engine was not found"));
//        EngineDto foundEngine = new EngineDto();
//        foundEngine.setType(engine.getType());
//
//        return foundEngine;
//    }
//
////    @Override
////    public HashSet<CarDto> getAllCarsWithTypeIdEngine(UUID engineId) {
////        Set<ListingCar> cars = listingCarRepository.getAllSet();
////        HashSet<CarDto> hashSetOfCars = (HashSet<CarDto>) cars.stream().map(c -> {
////            if (c.getEngine().getId().equals(engineId)){
////                return mapToDtoCar(c);
////            }
////            return null;
////        }).collect(Collectors.toSet());
////
////        return hashSetOfCars;
////    }
//
////    private CarDto mapToDtoCar(ListingCar car){
////        CarDto carDto = new CarDto();
////
////        carDto.setId(car.getId());
////        carDto.setBrand(car.getBrand());
////        carDto.setModel(car.getModel());
////        carDto.setColor(car.getColor());
////        carDto.setMileage(car.getMileage());
////        carDto.setEngineType(car.getEngine().getType());
////
////        return carDto;
////    }
////
////    private ListingCar mapToEntityCar(CarDto carDto){
////        ListingCar car = new ListingCar();
////
////        car.setId(carDto.getId());
////        car.setBrand(carDto.getBrand());
////        car.setModel(carDto.getModel());
////        car.setColor(car.getColor());
////        car.setMileage(car.getMileage());
////
////        return car;
////    }
//}
