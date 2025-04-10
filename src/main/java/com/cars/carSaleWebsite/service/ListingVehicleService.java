package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.Listing.CRUD.FormOptionsDto;
import com.cars.carSaleWebsite.dto.Listing.CarPaginationResponse;
import com.cars.carSaleWebsite.dto.Listing.CRUD.CreateCarListingDto;
import com.cars.carSaleWebsite.dto.Listing.FilterDto;
import com.cars.carSaleWebsite.dto.Listing.ListingCarDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ListingVehicleService {
    Map<String, Object> getListingById(UUID id);
    String createCarListing(CreateCarListingDto car, List<MultipartFile> images) throws IOException;
    HashSet<ListingCarDto> getAllCars();
    CarPaginationResponse getByPage(int pageNo, int pageSize);
    String deleteCarById(UUID id);
    String updateCar(CreateCarListingDto carDto, UUID id) throws IOException;
    CarPaginationResponse searchCarByCriteria(FilterDto searchDto, int pageNo, int pageSize);
    boolean canAccessListing(String listingId);
    FormOptionsDto getAllFormOptions();
}
