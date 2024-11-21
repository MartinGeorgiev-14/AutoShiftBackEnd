//package com.cars.carSaleWebsite.service.impl;
//
//import com.cars.carSaleWebsite.dto.CarDto;
//import com.cars.carSaleWebsite.dto.CarPaginationResponse;
//import com.cars.carSaleWebsite.exceptions.CarNotFoundException;
//import com.cars.carSaleWebsite.models.entities.listing.ListingCar;
//import com.cars.carSaleWebsite.repository.ListingCarRepository;
//import com.cars.carSaleWebsite.service.ListingCarService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//
//import org.springframework.data.domain.Pageable;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//public class ListingCarServiceImpl implements ListingCarService {
//
//    private ListingCarRepository listingCarRepository;
//
//    @Autowired
//    public ListingCarServiceImpl(ListingCarRepository listingCarRepository) {
//        this.listingCarRepository = listingCarRepository;
//    }
//
////    @Override
////    public CarDto createCar(CarDto carDto) {
////        ListingCar car = new ListingCar();
////        car.setBrand(carDto.getBrand());
////        car.setModel(carDto.getModel());
////        car.setColor(carDto.getColor());
////        car.setMileage(carDto.getMileage());
////
////        ListingCar newCar = listingCarRepository.save(car);
////
////        CarDto carResponse = new CarDto();
////        System.out.println(newCar.getId());
////        carResponse.setId(newCar.getId());
////        carResponse.setBrand(newCar.getBrand());
////        carResponse.setModel(newCar.getModel());
////        carResponse.setColor(newCar.getColor());
////        carResponse.setMileage(newCar.getMileage());
////
////        return carResponse;
////    }
//
//    @Override
//    public List<CarDto> getAllCar() {
//        List<ListingCar> car = listingCarRepository.findAll();
//        return car.stream().map(c -> mapToDtop(c)).collect(Collectors.toList());
//    }
//
//    @Override
//    public CarPaginationResponse getByPage(int pageNo, int pageSize) {
//        Pageable pageable = PageRequest.of(pageNo, pageSize);
//        Page<ListingCar> cars = listingCarRepository.findAll(pageable);
//        List<ListingCar> listOfCar = cars.getContent();
//        List<CarDto> content = listOfCar.stream().map(c -> mapToDtop(c)).collect(Collectors.toList());
//
//        CarPaginationResponse carPaginationResponse = new CarPaginationResponse();
//        carPaginationResponse.setContent(content);
//        carPaginationResponse.setPageNo(cars.getNumber());
//        carPaginationResponse.setPageSize(cars.getSize());
//        carPaginationResponse.setTotalPages(cars.getTotalPages());
//        carPaginationResponse.setTotalElements(cars.getTotalElements());
//        carPaginationResponse.setFirst(cars.isFirst());
//        carPaginationResponse.setLast(cars.isLast());
//
//        return carPaginationResponse;
//    }
//
//    @Override
//    public CarDto getCarById(UUID id) {
//        ListingCar car = listingCarRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car was not found"));
//        return mapToDtop(car);
//    }
//
//    @Override
//    public CarDto updateCar(CarDto carDto, UUID id) {
//        ListingCar car = listingCarRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car could not be updated"));
//
//        car.setBrand(carDto.getBrand());
//        car.setModel(carDto.getModel());
//        car.setColor(carDto.getColor());
//        car.setMileage(carDto.getMileage());
//
//        ListingCar updatedCar = listingCarRepository.save(car);
//
//        return mapToDtop(updatedCar);
//    }
//
//    @Override
//    public void deleteCar(UUID id) {
//        ListingCar car = listingCarRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car could not be deleted"));
//        listingCarRepository.delete(car);
//    }
//
//    @Override
//    public Set<CarDto> getAllCarSet() {
//        HashSet<ListingCar> cars = listingCarRepository.getAllSet();
//        return cars.stream().map(c -> mapToDtop(c)).collect(Collectors.toSet());
//    }
//
//    private CarDto mapToDtop(ListingCar car){
//        CarDto carDto = new CarDto();
//
//        carDto.setId(car.getId());
//        carDto.setBrand(car.getBrand());
//        carDto.setModel(car.getModel());
//        carDto.setColor(car.getColor());
//        carDto.setMileage(car.getMileage());
//
//        return carDto;
//    }
//
//    private ListingCar mapToEntity(CarDto carDto){
//        ListingCar car = new ListingCar();
//
//        car.setId(carDto.getId());
//        car.setBrand(carDto.getBrand());
//        car.setModel(carDto.getModel());
//        car.setColor(car.getColor());
//        car.setMileage(car.getMileage());
//
//        return car;
//    }
//}
