package com.cars.carSaleWebsite.controllers;

import com.cars.carSaleWebsite.dto.Listing.CRUD.FormOptionsDto;
import com.cars.carSaleWebsite.dto.Listing.CarPaginationResponse;
import com.cars.carSaleWebsite.dto.Listing.CRUD.CreateCarListingDto;
import com.cars.carSaleWebsite.dto.Listing.FilterDto;
import com.cars.carSaleWebsite.dto.Listing.ListingCarDto;
import com.cars.carSaleWebsite.models.nonEntity.Message;
import com.cars.carSaleWebsite.service.ListingVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/api/")
public class ListingCarController {

    private ListingVehicleService listingVehicleService;

    @Autowired
    public ListingCarController(ListingVehicleService listingVehicleService) {
        this.listingVehicleService = listingVehicleService;
    }

    @GetMapping("app")
    public ResponseEntity<HashSet<ListingCarDto>> getCars(){
        return new ResponseEntity<>(listingVehicleService.getAllCars(), HttpStatus.OK);
    }

    @GetMapping("app/{id}")
    public ResponseEntity<Map<String, Object>> getCarById(@PathVariable UUID id){
        Map<String, Object> body = listingVehicleService.getListingById(id);
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("app/options")
    public ResponseEntity<Map<String, Object>> getFormOptions(){

        Map<String, Object> body = listingVehicleService.getAllFormOptions();
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("app/page")
    public ResponseEntity<Map<String, Object>> getCarsByPage(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "Price", required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "ASC", required = false) String sortDirection) {

        Map<String, Object> body = listingVehicleService.getByPage(pageNo, pageSize, sortBy, sortDirection);
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping("app/search")
    public ResponseEntity<CarPaginationResponse> getListings(
            @RequestBody(required = false) FilterDto filterDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {



        return new ResponseEntity<>(listingVehicleService.searchCarByCriteria(filterDto, page, size), HttpStatus.OK);


    }

    @PostMapping(path = "app/create", consumes = {"multipart/form-data"})
    public ResponseEntity<String> postCar(@ModelAttribute CreateCarListingDto car,
                                          @RequestPart("uploadImages") List<MultipartFile> uploadImages) throws IOException {
        return new ResponseEntity<>(listingVehicleService.createCarListing(car, uploadImages), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or @listingCarService.canAccessListing(#listingId)")
    @DeleteMapping("app/delete/{listingId}")
    public ResponseEntity<String> deleteCarById(@PathVariable UUID listingId){
        String response = listingVehicleService.deleteCarById(listingId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @listingCarService.canAccessListing(#listingId)")
    @PatchMapping(path = "app/update/{listingId}")
    public ResponseEntity<String> updateCar(@RequestBody CreateCarListingDto car, @PathVariable UUID listingId) throws IOException {

        return new ResponseEntity<>(listingVehicleService.updateCar(car, listingId), HttpStatus.OK);
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
