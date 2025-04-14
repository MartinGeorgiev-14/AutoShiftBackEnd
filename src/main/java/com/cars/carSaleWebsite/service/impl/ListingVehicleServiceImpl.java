package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.Authentication.UserEntityDto;
import com.cars.carSaleWebsite.dto.Listing.CRUD.CreateCarListingDto;
import com.cars.carSaleWebsite.dto.Listing.CRUD.FormOptionsDto;
import com.cars.carSaleWebsite.dto.Listing.*;
import com.cars.carSaleWebsite.exceptions.NotFoundException;
import com.cars.carSaleWebsite.helpers.BodyCreator;
import com.cars.carSaleWebsite.helpers.ListingSpecification;
import com.cars.carSaleWebsite.helpers.MessageCreator;
import com.cars.carSaleWebsite.mappers.FilterMapper;
import com.cars.carSaleWebsite.mappers.ListingCarMapper;
import com.cars.carSaleWebsite.mappers.ListingImageMapper;
import com.cars.carSaleWebsite.mappers.UserEntityMapper;
import com.cars.carSaleWebsite.models.entities.listing.ListingImage;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.vehicle.*;
import com.cars.carSaleWebsite.models.nonEntity.Message;
import com.cars.carSaleWebsite.repository.*;
import com.cars.carSaleWebsite.security.JWTGenerator;
import com.cars.carSaleWebsite.service.ListingVehicleService;
import com.cars.carSaleWebsite.service.ListingImageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service("listingCarService")
public class ListingVehicleServiceImpl implements ListingVehicleService {

   private final ListingVehicleRepository listingVehicleRepository;
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
   private final BodyCreator bodyCreator;
   private final MessageCreator messageCreator;
   private final EuroStandardRepository euroStandardRepository;
   private final ColorRepository colorRepository;

    @Autowired
    public ListingVehicleServiceImpl(ListingVehicleRepository listingVehicleRepository, UserEntityRepository userEntityRepository,
                                     ListingCarMapper listingCarMapper, ListingImageRepository listingImageRepository,
                                     UserEntityMapper userEntityMapper, ListingImageMapper listingImageMapper,
                                     ListingImageService listingImageService, ModelRepository modelRepository,
                                     EngineRepository engineRepository, GearboxRepository gearboxRepository,
                                     BodyRepository bodyRepository, LocationRepository locationRepository,
                                     ObjectMapper objectMapper, FilterMapper filterMapper,
                                     JWTGenerator jwtGenerator, TypeRepository typeRepository,
                                     MakeRepository makeRepository, RegionRepository regionRepository,
                                     BodyCreator bodyCreator, MessageCreator messageCreator, EuroStandardRepository euroStandardRepository, ColorRepository colorRepository) {
        this.listingVehicleRepository = listingVehicleRepository;
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
        this.bodyCreator = bodyCreator;
        this.messageCreator = messageCreator;
        this.euroStandardRepository = euroStandardRepository;
        this.colorRepository = colorRepository;
    }

    @Override
    public Map<String, Object> getListingById(UUID id) {
        try{
            ListingVehicle car = listingVehicleRepository.findCarById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing was not found"));
            UserEntity user = userEntityRepository.findById(car.getUserEntity().getId()).orElseThrow(() -> new UsernameNotFoundException("User was not found"));
            UserEntityDto mappedUser = userEntityMapper.toDTO(user);
            List<ListingImageDto> images = listingImageService.getAllImagesOfListingById(car);

            ListingCarDto mappedListing = listingCarMapper.toDTO(car, mappedUser, images);

            Map<String, Object> body = bodyCreator.create();
            Message message = messageCreator.create(false, "Listing Found", "The listing you've searched has been found", "success");

            body.put("listing", mappedListing);
            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;
        } catch (ResponseStatusException ex) {
            return createErrorResponse(ex.getReason(), ex.getStatusCode());
        }
        catch (Exception ex){
            return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public String createCarListing(CreateCarListingDto car, List<MultipartFile> images) throws IOException {

            String userId = getCurrentUserId();

            UserEntity user = userEntityRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new NotFoundException("User was not found"));
            Model model = modelRepository.findById(car.getModel()).orElseThrow(() -> new NotFoundException("Model not found"));
            Engine engine = engineRepository.findById(car.getEngine()).orElseThrow(() -> new NotFoundException("Engine not found"));
            Gearbox gearbox = gearboxRepository.findById(car.getGearbox()).orElseThrow(() -> new NotFoundException("Gearbox not found"));
            Body body = bodyRepository.findById(car.getBody()).orElseThrow(() -> new NotFoundException("Body not found"));
            Location location = locationRepository.findById(car.getLocation()).orElseThrow(() -> new NotFoundException("Location is not found"));

            ListingVehicle newListing = listingCarMapper.toEntity(car, user, model, engine, gearbox, body, location);

            listingVehicleRepository.save(newListing);

            if (!(images.getFirst().isEmpty())) {
                for (int i = 0; i < images.size(); i++) {
                    ListingImage listImage = new ListingImage();

                    listImage.setType(images.get(i).getContentType());
//                    listImage.setImageData(images.get(i).getBytes());
                    listImage.setListingId(newListing);
                    listImage.setMain(true);

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
       HashSet<ListingVehicle> cars = listingVehicleRepository.getAllCars();

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
    public Map<String, Object> getByPage(int pageNo, int pageSize, String sortBy, String sortDirection) {
        try{
            String userId = getCurrentUserId();
            UserEntity user = userEntityRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found"));
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            Pageable pageRequest = PageRequest.of(pageNo, pageSize, sort);
            Page<ListingVehicle> listings = listingVehicleRepository.findAllByUserEntity(user, pageRequest);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {

            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    listings = listingVehicleRepository.findAll(pageRequest);
                }
            }
        }

            HashSet<ListingCarDto> content = listings.stream().map(c -> {
                List<ListingImage> images = listingImageRepository.getAllListingImagesByListing(c);
                List<ListingImageDto> mappedImages =  listingImageService.getAllImagesOfListingById(c);
                UserEntityDto mappeedUser = userEntityMapper.toDTO(user);
                ListingCarDto mapped = listingCarMapper.toDTO(c, mappeedUser, mappedImages);

                return mapped;
            }).collect(Collectors.toCollection(HashSet::new));

            CarPaginationResponse response = listingCarMapper.toPegination(listings, content);

            Map<String, Object> body = bodyCreator.create();
            Message message = messageCreator.create(false, "Listings Found", "The listing you've searched have been found", "success");

            body.put("listings", response);
            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;
        } catch (ResponseStatusException ex) {
            return createErrorResponse(ex.getReason(), ex.getStatusCode());
        }
        catch (Exception ex){
            return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    @Transactional
    public String deleteCarById(UUID id) {

        ListingVehicle car = listingVehicleRepository.findCarById(id).orElseThrow(() -> new NotFoundException("Listing was not found"));
        listingImageRepository.deleteListingById(car);
        listingVehicleRepository.deleteById(car.getId());

        return "Listing has been deleted successfully";
    }

    @Override
    @Transactional
    public String updateCar(CreateCarListingDto carDto ,UUID id) throws IOException {
        ListingVehicle nowcar = listingVehicleRepository.findCarById(id).orElseThrow(() -> new NotFoundException("Car was not found"));

        //Modifying listing
        UserEntity user = userEntityRepository.findById(UUID.fromString(nowcar.getUserEntity().getId().toString())).orElseThrow(() -> new NotFoundException("User was not found"));
        Model model = modelRepository.findByIdOrNull(carDto.getModel()).orElse(null);
        Engine engine = engineRepository.findByIdOrNull(carDto.getEngine()).orElse(null);
        Gearbox gearbox = gearboxRepository.findByIdOrNull(carDto.getGearbox()).orElse(null);
        Body body = bodyRepository.findByIdOrNull(carDto.getBody()).orElse(null);
        Location location = locationRepository.findByIdOrNull(carDto.getLocation()).orElse(null);

        ListingVehicle newcar = listingCarMapper.toEntity(carDto, user, model, engine, gearbox, body, location);
        newcar.setId(nowcar.getId());

        patch(nowcar, newcar);

        listingVehicleRepository.save(nowcar);

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





        return "Listing has been updated";
    }

    @Transactional
    @Override
    public CarPaginationResponse searchCarByCriteria(FilterDto filterDto, int pageNo, int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, "price"));


        Page<ListingVehicle> listings = listingVehicleRepository.findAll(
                ListingSpecification.filters(filterDto),
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

        ListingVehicle vehicle = listingVehicleRepository.findCarById(UUID.fromString(listingId)).orElse(null);

        if (vehicle == null) {
            return false;
        }

        UUID listingOwnerId = vehicle.getUserEntity().getId();

        return listingOwnerId.equals(UUID.fromString(userId));

    }

    @Override
    public Map<String, Object> getAllFormOptions() {
        try{
            FormOptionsDto options = new FormOptionsDto();

            HashSet<Engine> engines = engineRepository.getAll();
            HashSet<Gearbox> gearboxes = gearboxRepository.getAll();

            HashSet<Type> types = typeRepository.getAll();
            HashSet<Body> bodies = bodyRepository.getAll();

            HashSet<Make> makes = makeRepository.getAll();
            HashSet<Model> models = modelRepository.getAll();

            HashSet<Region> regions = regionRepository.getAll();
            HashSet<Location> locations = locationRepository.getAll();

            HashSet<EuroStandard> euroStandards = euroStandardRepository.getAll();

            HashSet<Color> colors = colorRepository.getAll();

            options.setEngineOptions(engines);
            options.setGearboxOptions(gearboxes);

            options.setTypeOptions(types);
            options.setBodyOptions(bodies);

            options.setMakeOptions(makes);
            options.setModelOptions(models);

            options.setRegionOptions(regions);
            options.setLocationOptions(locations);

            options.setEuroStandardOptions(euroStandards);

            options.setColorOptions(colors);

            Map<String, Object> body = bodyCreator.create();
            Message message = messageCreator.create(false, "Options Found", "The options you've searched have been found", "success");

            body.put("options", options);
            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;
        } catch (ResponseStatusException ex) {
            return createErrorResponse(ex.getReason(), ex.getStatusCode());
        }
        catch (Exception ex){
            return createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
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

        if(token == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You need to login in order to see your listings");
        }

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

    private Map<String, Object> createErrorResponse(String reason, HttpStatusCode status){
        Map<String, Object> body = bodyCreator.create();

        Message message = messageCreator.create(true, "Error", reason, "error");
        body.put("message", message);
        body.put("status", status.value());

        return body;
    }
}

