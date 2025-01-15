package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.*;
import com.cars.carSaleWebsite.dto.FormOptionsDto;
import com.cars.carSaleWebsite.exceptions.NotFoundException;
import com.cars.carSaleWebsite.helpers.ListingSpecification;
import com.cars.carSaleWebsite.mappers.FilterMapper;
import com.cars.carSaleWebsite.mappers.ListingCarMapper;
import com.cars.carSaleWebsite.mappers.ListingImageMapper;
import com.cars.carSaleWebsite.mappers.UserEntityMapper;
import com.cars.carSaleWebsite.models.entities.listing.ListingImage;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.vehicle.*;
import com.cars.carSaleWebsite.repository.*;
import com.cars.carSaleWebsite.security.JWTGenerator;
import com.cars.carSaleWebsite.service.ListingCarService;
import com.cars.carSaleWebsite.service.ListingImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service("listingCarService")
public class ListingCarServiceImpl implements ListingCarService {

   private final ListingCarRepository listingCarRepository;
   private final UserEntityRepository userEntityRepository;
   private final ListingCarMapper listingCarMapper;
   private final ListingImageRepository listingImageRepository;
   private final UserEntityMapper userEntityMapper;
   private final ListingImageMapper listingImageMapper;
   private final ListingImageService listingImageService;
   private final ModelRepository modelRepository;
   private final EngineRepository engineRepository;
   private final GearboxRepository gearboxRepository;
   private final BodyRepository bodyRepository;
   private final LocationRepository locationRepository;
   private final ObjectMapper objectMapper;
   private final FilterMapper filterMapper;
   private final JWTGenerator jwtGenerator;
   private final TypeRepository typeRepository;
   private final MakeRepository makeRepository;
   private final RegionRepository regionRepository;

    @Autowired
    public ListingCarServiceImpl(ListingCarRepository listingCarRepository, UserEntityRepository userEntityRepository,
                                 ListingCarMapper listingCarMapper, ListingImageRepository listingImageRepository,
                                 UserEntityMapper userEntityMapper, ListingImageMapper listingImageMapper,
                                 ListingImageService listingImageService, ModelRepository modelRepository,
                                 EngineRepository engineRepository, GearboxRepository gearboxRepository,
                                 BodyRepository bodyRepository, LocationRepository locationRepository,
                                 ObjectMapper objectMapper, FilterMapper filterMapper,
                                 JWTGenerator jwtGenerator, TypeRepository typeRepository,
                                 MakeRepository makeRepository, RegionRepository regionRepository) {
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
        this.objectMapper = objectMapper;
        this.filterMapper = filterMapper;
        this.jwtGenerator = jwtGenerator;
        this.typeRepository = typeRepository;
        this.makeRepository = makeRepository;
        this.regionRepository = regionRepository;
    }

    @Override
    @Transactional
    public ListingCarDto getCarById(UUID id) {

        ListingVehicle car = listingCarRepository.findCarById(id).orElseThrow(() -> new NotFoundException("Listing was not found"));
        UserEntity user = userEntityRepository.findById(car.getUserEntity().getId()).orElseThrow(() -> new UsernameNotFoundException("User was not found"));
        UserEntityDto mappedUser = userEntityMapper.toDTO(user);
        List<ListingImageDto> images = listingImageService.getAllImagesOfListingById(car);

        ListingCarDto mappedListing = listingCarMapper.toDTO(car, mappedUser, images);

        return mappedListing;
    }

    @Override
    @Transactional
    public String createCarListing(ListingCarDto car, List<MultipartFile> images) throws IOException {

            String userId = getCurrentUserId();

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
    @Transactional
    public String deleteCarById(UUID id) {

        ListingVehicle car = listingCarRepository.findCarById(id).orElseThrow(() -> new NotFoundException("Listing was not found"));
        listingImageRepository.deleteListingById(car);
        listingCarRepository.deleteById(car.getId());

        return "Listing has been deleted successfully";
    }

    @Override
    @Transactional
    public String updateCar(ListingCarDto carDto ,UUID id) throws IOException {
        ListingVehicle nowcar = listingCarRepository.findCarById(id).orElseThrow(() -> new NotFoundException("Car was not found"));

        //Modifying listing
        UserEntity user = userEntityRepository.findById(UUID.fromString(nowcar.getUserEntity().getId().toString())).orElseThrow(() -> new NotFoundException("User was not found"));
        Optional<Model> model = modelRepository.findByModelName(carDto.getModel());
        Optional<Engine> engine = engineRepository.findEngineByType(carDto.getEngine());
        Optional<Gearbox> gearbox = gearboxRepository.findGearboxByType(carDto.getGearbox());
        Optional<Body> body = bodyRepository.findByBodyType(carDto.getBody());
        Optional<Location> location = locationRepository.findByRegion(carDto.getLocation());

        ListingVehicle newcar = listingCarMapper.toEntity(carDto, user, model.orElse(null), engine.orElse(null), gearbox.orElse(null), body.orElse(null), location.orElse(null));
        newcar.setId(nowcar.getId());

        patch(nowcar, newcar);

        listingCarRepository.save(nowcar);

        //Modifying images
        List<ListingImage> imagesDb = listingImageRepository.getAllListingImagesByListing(newcar);
//        ListingImage newMainImg = listingImageRepository.findById(carDto.getMainImgId()).orElse(null);
        Optional<ListingImage> newMainImg = Optional.ofNullable(carDto.getMainImgId())
                .flatMap(listingImageRepository::findById);


        if (imagesDb.contains(newMainImg)){
            for (ListingImage image : imagesDb){
                image.setMain(false);

                if (image.equals(newMainImg)){
                    image.setMain(true);
                }
            }
        }


//
//        if (!(images.getFirst().isEmpty())) {
//            for (MultipartFile image : images) {
//                ListingImage listImage = new ListingImage();
//                Boolean isFound = false;
//
//                if(imagesDb.size() > 8){
//                    break;
//                }
//
//                for(ListingImage dbImage : imagesDb){
//
//                    if(image.getBytes().equals(dbImage.getImageData())){
//                        isFound = true;
//                    }
//                    //tuka trqbva do pravish problema
//
//                }
//
//                if(isFound){
//                    listImage.setType(image.getContentType());
//                    listImage.setImageData(image.getBytes());
//                    listImage.setListingId(nowcar);
//                    listingImageRepository.save(listImage);
//                }
//            }
//        }
//
//        imagesDb = listingImageRepository.getAllListingImagesByListing(newcar);
//
//        if(carDto.getMainImgId() != null){
//            for (ListingImage image : imagesDb){
//                if (image.getId().equals(carDto.getMainImgId()) && nowcar.getId() == id){
//                    image.setMain(true);
//                }
//                else{
//                    image.setMain(false);
//                }
//
//                listingImageRepository.save(image);
//            }
//        }
//

        //doesnt work

//        if(carDto.getMainImgIndex() >= 0 && images.size() >= carDto.getMainImgIndex()){
//            for (int i = 0; i < images.size(); i++){
//                ListingImage image = listingImageRepository.getReferenceById(images.get(i).getId());
//
//                if(i == carDto.getMainImgIndex()){
//                    image.setMain(true);
//                }
//                else{
//                    image.setMain(false);
//                }
//
//
//                listingImageRepository.save(image);
//            }
//        }



        return "Listing has been updated";
    }

    @Transactional
    @Override
    public CarPaginationResponse searchCarByCriteria(FilterDto filterDto, int pageNo, int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(new Sort.Order(Sort.Direction.ASC, "price")));

        Page<ListingVehicle> listings = listingCarRepository.findAll(
                ListingSpecification.withFilters(filterDto),
                pageRequest);

        HashSet<ListingCarDto> content = (HashSet<ListingCarDto>) listings.stream().map(c -> {
            UserEntity user = userEntityRepository.findById(c.getUserEntity().getId()).orElseThrow(() -> new UsernameNotFoundException("User was not found"));
            UserEntityDto mappedUser = userEntityMapper.toDTO(user);
            List<ListingImageDto> images = listingImageService.getAllImagesOfListingById(c);

            ListingCarDto mappedListing = listingCarMapper.toDTO(c, mappedUser, images);

            return mappedListing;
        }).collect(Collectors.toSet());


        CarPaginationResponse response = new CarPaginationResponse();
        response.setContent(content);
        response.setPageNo(listings.getNumber());
        response.setPageSize(listings.getSize());
        response.setTotalPages(listings.getTotalPages());
        response.setTotalElements(listings.getTotalElements());
        response.setFirst(listings.isFirst());
        response.setLast(listings.isLast());

        return response;
    }

    @Override
    public boolean canAccessListing(String listingId) {
        String userId = getCurrentUserId();

        return listingCarRepository.findCarById(UUID.fromString(listingId))
                .map(l -> l.getUserEntity().getId().equals(userId)).orElse(false);

    }

    @Override
    public FormOptionsDto getAllFormOptions() {
        FormOptionsDto options = new FormOptionsDto();

        HashSet<Engine> engines = engineRepository.getAll();
        HashSet<Gearbox> gearboxes = gearboxRepository.getAll();

        HashSet<Type> types = typeRepository.getAll();
        HashSet<Body> bodies = bodyRepository.getAll();

        HashSet<Make> makes = makeRepository.getAll();
        HashSet<Model> models = modelRepository.getALl();

        HashSet<Region> regions = regionRepository.getAll();
        HashSet<Location> locations = locationRepository.getAll();

        options.setEngineOptions(engines);
        options.setGearboxOptions(gearboxes);

        options.setTypeOptions(types);
        options.setBodyOptions(bodies);

        options.setMakeOptions(makes);
        options.setModelOptions(models);

        options.setRegionOptions(regions);
        options.setLocationOptions(locations);

        return options;
    }


    private void patch(ListingVehicle oldInstance, ListingVehicle newInstance) {
        Field[] fields = oldInstance.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Enable access to private fields
            try {
                Object newValue = field.get(newInstance);
                if (newValue != null) {
                    field.set(oldInstance, newValue); // Update the old instance
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to patch field: " + field.getName(), e);
            }
        }
    }

    private String getImageTypeFromFileName(String fileName) {
        int index = fileName.lastIndexOf('.');
        return (index > 0) ? fileName.substring(index + 1).toLowerCase() : "unknown";
    }

    private String getCurrentUserId(){
        String token = (String) getJWTFromRequest();
        return jwtGenerator.getUserIdFromJWT(token);
    }

    private String getJWTFromRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String bearerToken = request.getHeader("Authorization");
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                return bearerToken.substring(7);
            }
        }
        return null;
    }

}

