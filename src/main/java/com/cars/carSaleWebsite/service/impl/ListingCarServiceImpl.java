package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.ListingCarDto;
import com.cars.carSaleWebsite.dto.ListingImageDto;
import com.cars.carSaleWebsite.dto.UserEntityDto;
import com.cars.carSaleWebsite.mappers.ListingCarMapper;
import com.cars.carSaleWebsite.mappers.ListingImageMapper;
import com.cars.carSaleWebsite.mappers.UserEntityMapper;
import com.cars.carSaleWebsite.models.entities.listing.ListingImage;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.vehicle.Body;
import com.cars.carSaleWebsite.models.entities.vehicle.Engine;
import com.cars.carSaleWebsite.models.entities.vehicle.Gearbox;
import com.cars.carSaleWebsite.models.entities.vehicle.Model;
import com.cars.carSaleWebsite.repository.*;
import com.cars.carSaleWebsite.service.ListingCarService;
import com.cars.carSaleWebsite.service.ListingImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
   private ModelRepository modelRepository;
   private EngineRepository engineRepository;
   private GearboxRepository gearboxRepository;
   private BodyRepository bodyRepository;

    @Autowired
    public ListingCarServiceImpl(ListingCarRepository listingCarRepository, UserEntityRepository userEntityRepository,
                                 ListingCarMapper listingCarMapper, ListingImageRepository listingImageRepository,
                                 UserEntityMapper userEntityMapper, ListingImageMapper listingImageMapper,
                                 ListingImageService listingImageService, ModelRepository modelRepository,
                                 EngineRepository engineRepository, GearboxRepository gearboxRepository,
                                 BodyRepository bodyRepository) {
        this.listingCarRepository = listingCarRepository;
        this.userEntityRepository = userEntityRepository;
        this.listingCarMapper = listingCarMapper;
        this.listingImageRepository = listingImageRepository;
        this.userEntityMapper = userEntityMapper;
        this.listingImageMapper = listingImageMapper;
        this.listingImageService = listingImageService;
        this.modelRepository = modelRepository;
        this.engineRepository = engineRepository;
        this.gearboxRepository = gearboxRepository;
        this.bodyRepository = bodyRepository;
    }

    @Override
    @Transactional
    public ListingCarDto getCarById(UUID id) {

        ListingVehicle car = listingCarRepository.findCarById(id);
        UserEntity user = userEntityRepository.findById(car.getUserEntity().getId()).orElseThrow(() -> new UsernameNotFoundException("User was not found"));
        UserEntityDto mappedUser = userEntityMapper.toDTO(user);
        List<ListingImageDto> images = listingImageService.getAllImagesOfListingById(car);

        ListingCarDto mappedListing = listingCarMapper.toDTO(car, mappedUser, images);

        return mappedListing;
    }

    @Override
    @Transactional
    public String createCarListing(ListingCarDto car, List<MultipartFile> images) throws IOException {

        try {
            UserEntity user = userEntityRepository.findById(car.getUser().getId()).orElseThrow(() -> new UsernameNotFoundException("User was not found"));
            Model model = modelRepository.findByModelName(car.getModel()).orElseThrow(() -> new RuntimeException("Model not found"));
            Engine engine = engineRepository.findEngineByType(car.getEngine()).orElseThrow(() -> new RuntimeException("Engine not found"));
            Gearbox gearbox = gearboxRepository.findGearboxByType(car.getGearbox()).orElseThrow(() -> new RuntimeException("Gearbox not found"));
            Body body = bodyRepository.findByBodyType(car.getBody()).orElseThrow(() -> new RuntimeException("Body not found"));

            ListingVehicle newListing = listingCarMapper.toEntity(car, user, model, engine, gearbox, body);

            listingCarRepository.save(newListing);

            if (!images.isEmpty()) {
                for (int i = 0; i <= images.size(); i++) {
                    ListingImage listImage = new ListingImage();

                    listImage.setType(images.get(i).getContentType());
                    listImage.setImageData(images.get(i).getBytes());
                    listImage.setListingId(newListing);

                    if (car.getMainImgIndex() == i) {
                        listImage.setMain(true);
                    } else {
                        listImage.setMain(false);
                    }

                    listingImageRepository.save(listImage);

                }
            }
            return "The car has been saved successfully!";
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    private String getImageTypeFromFileName(String fileName) {
        int index = fileName.lastIndexOf('.');
        return (index > 0) ? fileName.substring(index + 1).toLowerCase() : "unknown";
    }


}

