package com.cars.carSaleWebsite.controllers;

import com.cars.carSaleWebsite.dto.ListingImageDto;
import com.cars.carSaleWebsite.service.ListingImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.UUID;

@RequestMapping("/api/")
@Controller
public class ListingImagesController {

    ListingImageService listingImageService;

    @Autowired
    public ListingImagesController(ListingImageService listingImageService) {
        this.listingImageService = listingImageService;
    }

    @GetMapping("/images/{id}")
    public ResponseEntity<HashSet<ListingImageDto>> getImagesByListing(@PathVariable UUID id) {
        HashSet<ListingImageDto> images = listingImageService.getAllImagesOfListing(id);

        return new ResponseEntity<>(images, HttpStatus.OK);
    }

}
