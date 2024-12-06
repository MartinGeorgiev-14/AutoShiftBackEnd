package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.CarPaginationResponse;
import com.cars.carSaleWebsite.dto.ListingCarDto;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public interface ListingCarService {
    ListingCarDto getCarById(UUID id);
    String createCarListing(ListingCarDto car,String user, List<MultipartFile> images) throws IOException;
    HashSet<ListingCarDto> getAllCars();
    CarPaginationResponse getByPage(int pageNo, int pageSize);
    String deleteCarById(UUID id);
    String updateCar(ListingCarDto carDto, UUID id) throws JsonMappingException;
}
