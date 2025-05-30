package com.cars.carSaleWebsite.controllers;

import com.cars.carSaleWebsite.dto.Listing.CRUD.PatchCarListingDto;
import com.cars.carSaleWebsite.dto.Listing.CRUD.CreateCarListingDto;
import com.cars.carSaleWebsite.dto.Listing.CRUD.FilterDto;
import com.cars.carSaleWebsite.dto.Listing.ListingCarDto;
import com.cars.carSaleWebsite.service.ListingVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
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
            @RequestParam(value = "sortDirection", defaultValue = "ASC", required = false) String sortDirection,
            @RequestParam(value = "isActive", defaultValue = "true", required = false) Boolean isActive) {

        Map<String, Object> body = listingVehicleService.getByPage(pageNo, pageSize, sortBy, sortDirection, isActive);
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("app/page/createdAt")
    public ResponseEntity<Map<String, Object>> getCarsByPageSortedByCreatedAt(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){

        Map<String, Object> body = listingVehicleService.getByPageByCreatedAt(pageNo, pageSize);
        Integer status = (Integer) body.get("status");

        if(status != 200)
        {
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PostMapping("app/search")
    public ResponseEntity<Map<String, Object>> getListings(
            @RequestBody(required = false) FilterDto filterDto,
            @RequestParam(value = "pageNo", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "Price", required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "ASC", required = false) String sortDirection) {

        Map<String, Object> body = listingVehicleService.searchCarByCriteria(filterDto, page, size, sortBy, sortDirection);
        Integer status = (Integer) body.get("status");

        if(status != 200){
            return  new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("app/search/fromEmail")
    public ResponseEntity<Map<String, Object>> getListingsFromEmail(
            @RequestParam(value = "pageNo", defaultValue = "0") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "Price", required = false) String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "ASC", required = false) String sortDirection,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdStart,
            @RequestParam(required = false) UUID make,
            @RequestParam(required = false) UUID model,
            @RequestParam(required = false) UUID region,
            @RequestParam(required = false) UUID location,
            @RequestParam(required = false) UUID engine,
            @RequestParam(required = false) UUID gearbox,
            @RequestParam(required = false) UUID type,
            @RequestParam(required = false) UUID body,
            @RequestParam(required = false) UUID color,
            @RequestParam(required = false) UUID euroStandard,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate manufactureDateStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate manufactureDateEnd,
            @RequestParam(required = false) BigDecimal priceStart,
            @RequestParam(required = false) BigDecimal priceEnd,
            @RequestParam(required = false) Integer horsepowerStart,
            @RequestParam(required = false) Integer horsepowerEnd,
            @RequestParam(required = false) Integer mileageStart,
            @RequestParam(required = false) Integer mileageEnd,
            @RequestParam(required = false) Integer engineDisplacementStart,
            @RequestParam(required = false) Integer engineDisplacementEnd
            ) {

        FilterDto filterDto = new FilterDto();
        filterDto.setCreatedStart(createdStart);
        filterDto.setMake(make);
        filterDto.setModel(model);
        filterDto.setRegion(region);
        filterDto.setLocation(location);
        filterDto.setEngine(engine);
        filterDto.setGearbox(gearbox);
        filterDto.setType(type);
        filterDto.setBody(body);
        filterDto.setColor(color);
        filterDto.setEuroStandard(euroStandard);
        filterDto.setManufactureDateStart(manufactureDateStart);
        filterDto.setManufactureDateEnd(manufactureDateEnd);
        filterDto.setPriceStart(priceStart);
        filterDto.setPriceEnd(priceEnd);
        filterDto.setHorsepowerStart(horsepowerStart);
        filterDto.setHorsepowerEnd(horsepowerEnd);
        filterDto.setMileageStart(mileageStart);
        filterDto.setMileageEnd(mileageEnd);
        filterDto.setEngineDisplacementStart(engineDisplacementStart);
        filterDto.setEngineDisplacementEnd(engineDisplacementEnd);

        Map<String, Object> response = listingVehicleService.searchCarByCriteria(filterDto, page, size, sortBy, sortDirection);
        Integer status = (Integer) response.get("status");

        if(status != 200){
            return  new ResponseEntity<>(response, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path = "app/create", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> postCar(@ModelAttribute CreateCarListingDto car,
                                          @RequestPart("uploadImages") List<MultipartFile> uploadImages) throws IOException {

        Map<String, Object> body = listingVehicleService.createCarListing(car, uploadImages);
        Integer status = (Integer) body.get("status");

        if(status != 201){
            return  new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or @listingCarService.canAccessListing(#id)")
    @PatchMapping("app/update/status/{id}")
    public ResponseEntity<Map<String, Object>> changeStatusListing(@PathVariable("id") UUID id){

        Map<String, Object> body = listingVehicleService.changeStatusListing(id);
        Integer status = (Integer) body.get("status");

        if(status != 201){
            return  new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.CREATED);

    }

    //   @listingCarService.canAccessListing(#listingId)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("app/delete/{listingId}")
    public ResponseEntity<Map<String, Object>> deleteCarById(@PathVariable UUID listingId){

        Map<String, Object> body = listingVehicleService.deleteCarById(listingId);
        Integer status = (Integer) body.get("status");

        if(status != 200){
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or @listingCarService.canAccessListing(#listingId)")
    @PatchMapping(path = "app/update/{listingId}", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> updateCar(@ModelAttribute PatchCarListingDto car,
                                                         @PathVariable UUID listingId) throws IOException {

        Map<String, Object> body = listingVehicleService.updateCar(car, listingId);
        Integer status = (Integer) body.get("status");

        if(status != 200){
            return new ResponseEntity<>(body, HttpStatus.valueOf(status));
        }

        return new ResponseEntity<>(body, HttpStatus.OK);
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
