package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.CarPaginationResponse;
import com.cars.carSaleWebsite.dto.ListingCarDto;
import com.cars.carSaleWebsite.dto.ListingImageDto;
import com.cars.carSaleWebsite.dto.UserEntityDto;
import com.cars.carSaleWebsite.exceptions.NotFoundException;
import com.cars.carSaleWebsite.mappers.ListingCarMapper;
import com.cars.carSaleWebsite.mappers.ListingImageMapper;
import com.cars.carSaleWebsite.mappers.UserEntityMapper;
import com.cars.carSaleWebsite.models.entities.listing.ListingImage;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.vehicle.*;
import com.cars.carSaleWebsite.repository.*;
import com.cars.carSaleWebsite.service.ListingCarService;
import com.cars.carSaleWebsite.service.ListingImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.smartcardio.CardPermission;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
   private LocationRepository locationRepository;

    @Autowired
    public ListingCarServiceImpl(ListingCarRepository listingCarRepository, UserEntityRepository userEntityRepository,
                                 ListingCarMapper listingCarMapper, ListingImageRepository listingImageRepository,
                                 UserEntityMapper userEntityMapper, ListingImageMapper listingImageMapper,
                                 ListingImageService listingImageService, ModelRepository modelRepository,
                                 EngineRepository engineRepository, GearboxRepository gearboxRepository,
                                 BodyRepository bodyRepository, LocationRepository locationRepository) {
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
        this.locationRepository = locationRepository;
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
    public String createCarListing(ListingCarDto car,  String userId,  List<MultipartFile> images) throws IOException {

            UserEntity user = userEntityRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new NotFoundException("User was not found"));
            Model model = modelRepository.findByModelName(car.getModel()).orElseThrow(() -> new NotFoundException("Model not found"));
            Engine engine = engineRepository.findEngineByType(car.getEngine()).orElseThrow(() -> new NotFoundException("Engine not found"));
            Gearbox gearbox = gearboxRepository.findGearboxByType(car.getGearbox()).orElseThrow(() -> new NotFoundException("Gearbox not found"));
            Body body = bodyRepository.findByBodyType(car.getBody()).orElseThrow(() -> new NotFoundException("Body not found"));
            Location location = locationRepository.findByRegion(car.getLocation()).orElseThrow(() -> new NotFoundException("Location is not found"));

            ListingVehicle newListing = listingCarMapper.toEntity(car, user, model, engine, gearbox, body, location);

            listingCarRepository.save(newListing);

            if (!(images.getFirst().isEmpty())) {
                for (int i = 0; i < images.size(); i++) {
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
    }

    @Override
    public HashSet<ListingCarDto> getAllCars() {
       HashSet<ListingVehicle> cars = listingCarRepository.getAllCars();

       HashSet<ListingCarDto> mapped = (HashSet<ListingCarDto>) cars.stream().map(c -> {
           UserEntity user = userEntityRepository.findById(c.getUserEntity().getId()).orElseThrow(() -> new UsernameNotFoundException("User was not found"));
           UserEntityDto mappedUser = userEntityMapper.toDTO(user);
           List<ListingImageDto> images = listingImageService.getAllImagesOfListingById(c);

           ListingCarDto mappedListing = listingCarMapper.toDTO(c, mappedUser, images);

           return mappedListing;

       }).collect(Collectors.toSet());

       return mapped;
    }

    @Override
    public CarPaginationResponse getByPage(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<ListingVehicle> cars = listingCarRepository.findAll(pageable);
        List<ListingVehicle> listOfCar = cars.getContent();
        HashSet<ListingCarDto> content = (HashSet<ListingCarDto>) listOfCar.stream().map(c -> {
            UserEntity user = userEntityRepository.findById(c.getUserEntity().getId()).orElseThrow(() -> new UsernameNotFoundException("User was not found"));
            UserEntityDto mappedUser = userEntityMapper.toDTO(user);
            List<ListingImageDto> images = listingImageService.getAllImagesOfListingById(c);

            ListingCarDto mappedListing = listingCarMapper.toDTO(c, mappedUser, images);

            return mappedListing;
        }).collect(Collectors.toSet());

        CarPaginationResponse carPaginationResponse = new CarPaginationResponse();
        carPaginationResponse.setContent(content);
        carPaginationResponse.setPageNo(cars.getNumber());
        carPaginationResponse.setPageSize(cars.getSize());
        carPaginationResponse.setTotalPages(cars.getTotalPages());
        carPaginationResponse.setTotalElements(cars.getTotalElements());
        carPaginationResponse.setFirst(cars.isFirst());
        carPaginationResponse.setLast(cars.isLast());

        return carPaginationResponse;
    }

    @Override
    public String deleteCarById(UUID id) {

        return "";
    }


    private String getImageTypeFromFileName(String fileName) {
        int index = fileName.lastIndexOf('.');
        return (index > 0) ? fileName.substring(index + 1).toLowerCase() : "unknown";
    }


}

