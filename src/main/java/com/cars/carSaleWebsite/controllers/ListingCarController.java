//package com.cars.carSaleWebsite.controllers;
//
//import com.cars.carSaleWebsite.dto.CarDto;
//import com.cars.carSaleWebsite.dto.CarPaginationResponse;
//import com.cars.carSaleWebsite.service.ListingCarService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/")
//public class ListingCarController {
//
//    private ListingCarService listingCarService;
//
//    @Autowired
//    public ListingCarController(ListingCarService listingCarService) {
//        this.listingCarService = listingCarService;
//    }
//
//    @GetMapping("car")
//    public ResponseEntity<List<CarDto>> getCars(){
//        return new ResponseEntity<>(listingCarService.getAllCar(), HttpStatus.OK);
//    }
//
//    @GetMapping("car/set")
//    public ResponseEntity<Set<CarDto>> getAllCarSet(){
//        return new ResponseEntity<>(listingCarService.getAllCarSet(), HttpStatus.OK);
//    }
//
//    @GetMapping("car/page")
//    public ResponseEntity<CarPaginationResponse> getCarsByPage(
//            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
//            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize)
//    {
//        return new ResponseEntity<>(listingCarService.getByPage(pageNo, pageSize), HttpStatus.OK);
//    }
//
//    @GetMapping("car/{id}")
//    public ResponseEntity<CarDto> carDetail(@PathVariable UUID id) {
//        return new ResponseEntity<>(listingCarService.getCarById(id), HttpStatus.OK);
//    }
//
//    @PostMapping("car/create")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<CarDto> createCar(@RequestBody CarDto car){
//        return new ResponseEntity<>(listingCarService.createCar(car), HttpStatus.CREATED);
//    }
//
//    @PutMapping("car/{id}/update")
//    public ResponseEntity<CarDto> updateCar(@RequestBody CarDto carDto, @PathVariable("id") UUID id){
//        CarDto response = listingCarService.updateCar(carDto, id);
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @DeleteMapping("car/{id}/delete")
//    public ResponseEntity<String> deleteCar(@PathVariable("id") UUID id){
//        listingCarService.deleteCar(id);
//        return new ResponseEntity<>("The car has been deleted", HttpStatus.OK);
//    }
//}