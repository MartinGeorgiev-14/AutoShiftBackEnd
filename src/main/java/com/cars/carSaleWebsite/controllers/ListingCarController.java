package com.cars.carSaleWebsite.controllers;

import com.cars.carSaleWebsite.dto.CarPaginationResponse;
import com.cars.carSaleWebsite.dto.FilterDto;
import com.cars.carSaleWebsite.dto.FormOptionsDto;
import com.cars.carSaleWebsite.dto.ListingCarDto;
import com.cars.carSaleWebsite.service.ListingCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/")
public class ListingCarController {

    private ListingCarService listingCarService;

    @Autowired
    public ListingCarController(ListingCarService listingCarService) {
        this.listingCarService = listingCarService;
    }

    @GetMapping("car")
    public ResponseEntity<HashSet<ListingCarDto>> getCars(){
        return new ResponseEntity<>(listingCarService.getAllCars(), HttpStatus.OK);
    }

    @GetMapping("car/{id}")
    public ResponseEntity<ListingCarDto> getCarById(@PathVariable UUID id){
        ListingCarDto car = listingCarService.getCarById(id);

        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @GetMapping("car/options")
    public ResponseEntity<FormOptionsDto> getFormOptions(){

        FormOptionsDto options = listingCarService.getAllFormOptions();

        return new ResponseEntity<>(options, HttpStatus.OK);
    }

    @GetMapping("car/page")
    public ResponseEntity<CarPaginationResponse> getCarsByPage(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){

        return new ResponseEntity<>(listingCarService.getByPage(pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("car/search")
    public ResponseEntity<CarPaginationResponse> getListings(
            @RequestBody(required = false) FilterDto filterDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {



        return new ResponseEntity<>(listingCarService.searchCarByCriteria(filterDto, page, size), HttpStatus.OK);


    }

    @PostMapping(path = "car/create", consumes = {"multipart/form-data"})
    public ResponseEntity<String> postCar(@ModelAttribute ListingCarDto car,
                                          @RequestPart("uploadImages") List<MultipartFile> images) throws IOException {
        return new ResponseEntity<>(listingCarService.createCarListing(car, images), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or @listingCarService.canAccessListing(#listingId)")
    @DeleteMapping("car/delete/{listingId}")
    public ResponseEntity<String> deleteCarById(@PathVariable UUID listingId){
        String response = listingCarService.deleteCarById(listingId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @listingCarService.canAccessListing(#listingId)")
    @PatchMapping(path = "car/update/{listingId}")
    public ResponseEntity<String> updateCar(@RequestBody ListingCarDto car, @PathVariable UUID listingId) throws IOException {

        return new ResponseEntity<>(listingCarService.updateCar(car, listingId), HttpStatus.OK);
    }



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
}
