package com.cars.carSaleWebsite.controllers;

import com.cars.carSaleWebsite.models.entities.listing.ListingImage;
import com.cars.carSaleWebsite.repository.ListingImageRepository;
import com.cars.carSaleWebsite.service.ListingImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;

@RequestMapping("/api/")
@Controller
public class ListingImagesController {

    ListingImageService listingImageService;
    ListingImageRepository listingImageRepository;

    @Autowired
    public ListingImagesController(ListingImageService listingImageService, ListingImageRepository listingImageRepository) {
        this.listingImageService = listingImageService;
        this.listingImageRepository = listingImageRepository;
    }

//    @GetMapping("/images/{id}")
//    public ResponseEntity<HashSet<ListingImageDto>> getImagesByListing(@PathVariable UUID id) {
//        HashSet<ListingImageDto> images = listingImageService.getAllImagesOfListingById(id);
//
//        return new ResponseEntity<>(images, HttpStatus.OK);
//    }

    @GetMapping("/images")
    public ResponseEntity<HashSet<ListingImage>> getAllImages(){
        HashSet<ListingImage> images = listingImageRepository.findAllImages();

        return new ResponseEntity<>(images, HttpStatus.OK);
    }

}
