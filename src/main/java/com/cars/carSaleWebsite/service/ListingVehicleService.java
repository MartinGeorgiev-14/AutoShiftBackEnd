package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.Listing.CRUD.PatchCarListingDto;
import com.cars.carSaleWebsite.dto.Listing.CRUD.CreateCarListingDto;
import com.cars.carSaleWebsite.dto.Listing.CRUD.FilterDto;
import com.cars.carSaleWebsite.dto.Listing.ListingCarDto;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
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
    Map<String, Object> getByPage(int pageNo, int pageSize, String sortBy, String sortDirection, Boolean isActive);
    Map<String, Object> deleteCarById(UUID id);
    Map<String, Object> updateCar(PatchCarListingDto carDto, UUID id) throws IOException;
    Map<String, Object> searchCarByCriteria(FilterDto filterDto, int pageNo, int pageSize, String sortBy, String sortDirection);
    List<ListingVehicle> searchCarByCriteria(FilterDto filterDto);
    boolean canAccessListing(String listingId);
    Map<String, Object> getAllFormOptions();
    Map<String, Object> changeStatusListing(UUID id);
    Map<String, Object> getByPageByCreatedAt(int pageNo, int pageSize);
    Map<String, Object> getByPageHome(int pageNo, int pageSize, String sortBy, String sortDirection, Boolean isActive);
}
