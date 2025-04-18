package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.Listing.CRUD.FormOptionsDto;
import com.cars.carSaleWebsite.dto.Listing.CarPaginationResponse;
import com.cars.carSaleWebsite.dto.Listing.CRUD.CreateCarListingDto;
import com.cars.carSaleWebsite.dto.Listing.FilterDto;
import com.cars.carSaleWebsite.dto.Listing.ListingCarDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ListingVehicleService {
    Map<String, Object> getListingById(UUID id);
    Map<String, Object> createCarListing(CreateCarListingDto car, List<MultipartFile> images) throws IOException;
    HashSet<ListingCarDto> getAllCars();
    Map<String, Object> getByPage(int pageNo, int pageSize, String sortBy, String sortDirection);
    Map<String, Object> deleteCarById(UUID id);
    Map<String, Object> updateCar(CreateCarListingDto carDto, UUID id) throws IOException;
    Map<String, Object> searchCarByCriteria(FilterDto filterDto, int pageNo, int pageSize, String sortBy, String sortDirection);
    boolean canAccessListing(String listingId);
    Map<String, Object> getAllFormOptions();
}
