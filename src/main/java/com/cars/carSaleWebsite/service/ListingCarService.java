package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.ListingCarDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public interface ListingCarService {
    ListingCarDto getCarById(UUID id);
    String createCarListing(ListingCarDto car,String user, List<MultipartFile> images) throws IOException;
    @Query
    HashSet<ListingCarDto> getAllCars();
}
