package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.ListingCarDto;
import com.cars.carSaleWebsite.dto.ListingImageDto;
import com.cars.carSaleWebsite.dto.UserEntityDto;
import com.cars.carSaleWebsite.mappers.ListingCarMapper;
import com.cars.carSaleWebsite.mappers.ListingImageMapper;
import com.cars.carSaleWebsite.mappers.UserEntityMapper;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.repository.ListingCarRepository;
import com.cars.carSaleWebsite.repository.ListingImageRepository;
import com.cars.carSaleWebsite.repository.UserEntityRepository;
import com.cars.carSaleWebsite.service.ListingCarService;
import com.cars.carSaleWebsite.service.ListingImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;

@Service
public class ListingCarServiceImpl implements ListingCarService {

   private ListingCarRepository listingCarRepository;
   private UserEntityRepository userEntityRepository;
   private ListingCarMapper listingCarMapper;
   private ListingImageRepository listingImageRepository;
   private UserEntityMapper userEntityMapper;
   private ListingImageMapper listingImageMapper;
   private ListingImageService listingImageService;

    @Autowired
    public ListingCarServiceImpl(ListingCarRepository listingCarRepository, UserEntityRepository userEntityRepository,
                                 ListingCarMapper listingCarMapper, ListingImageRepository listingImageRepository,
                                 UserEntityMapper userEntityMapper, ListingImageMapper listingImageMapper,
                                 ListingImageService listingImageService) {
        this.listingCarRepository = listingCarRepository;
        this.userEntityRepository = userEntityRepository;
        this.listingCarMapper = listingCarMapper;
        this.listingImageRepository = listingImageRepository;
        this.userEntityMapper = userEntityMapper;
        this.listingImageMapper = listingImageMapper;
        this.listingImageService = listingImageService;
    }

    @Override
    public ListingCarDto getCarById(UUID id) {

        ListingVehicle car = listingCarRepository.findCarById(id);
        UserEntity user = userEntityRepository.findById(car.getUserEntity().getId()).orElseThrow(() -> new UsernameNotFoundException("User was not found"));
        UserEntityDto mappedUser = userEntityMapper.toDTO(user);
        HashSet<ListingImageDto> images = listingImageService.getAllImagesOfListingById(car);

        ListingCarDto mappedListing = listingCarMapper.toDTO(car, mappedUser, images);

        return mappedListing;
    }

}
