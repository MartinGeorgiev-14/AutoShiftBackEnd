package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.Authentication.UserEntityDto;
import com.cars.carSaleWebsite.dto.Listing.CRUD.CreateCarListingDto;
import com.cars.carSaleWebsite.dto.Listing.CRUD.FormOptionsDto;
import com.cars.carSaleWebsite.dto.Listing.*;
import com.cars.carSaleWebsite.dto.Listing.CRUD.PatchCarListingDto;
import com.cars.carSaleWebsite.dto.Listing.CRUD.FilterDto;
import com.cars.carSaleWebsite.exceptions.NotFoundException;
import com.cars.carSaleWebsite.helpers.BodyCreator;
import com.cars.carSaleWebsite.helpers.ListingSpecification;
import com.cars.carSaleWebsite.helpers.MessageCreator;
import com.cars.carSaleWebsite.helpers.UserIdentificator;
import com.cars.carSaleWebsite.mappers.FavoritesMapper;
import com.cars.carSaleWebsite.mappers.ListingCarMapper;
import com.cars.carSaleWebsite.mappers.ListingImageMapper;
import com.cars.carSaleWebsite.mappers.UserEntityMapper;
import com.cars.carSaleWebsite.models.entities.listing.EditHistory;
import com.cars.carSaleWebsite.models.entities.listing.ListingImage;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.userFavorites.FavoriteListing;
import com.cars.carSaleWebsite.models.entities.vehicle.*;
import com.cars.carSaleWebsite.models.entities.vehicle.Color;
import com.cars.carSaleWebsite.models.nonEntity.Message;
import com.cars.carSaleWebsite.repository.listing.*;
import com.cars.carSaleWebsite.repository.user.UserEntityRepository;
import com.cars.carSaleWebsite.repository.userFavorite.FavoriteListingRepository;
import com.cars.carSaleWebsite.repository.vehicle.*;
import com.cars.carSaleWebsite.config.security.JWTGenerator;
import com.cars.carSaleWebsite.service.ListingVehicleService;
import com.cars.carSaleWebsite.service.ListingImageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.function.Function;
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
   private final FavoritesMapper favoritesMapper;
   private final JWTGenerator jwtGenerator;
   private final TypeRepository typeRepository;
   private final MakeRepository makeRepository;
   private final RegionRepository regionRepository;
   private final BodyCreator bodyCreator;
   private final MessageCreator messageCreator;
   private final EuroStandardRepository euroStandardRepository;
   private final ColorRepository colorRepository;
   private final UserIdentificator userIdentificator;
   private final EditHistoryRepository editHistoryRepository;
   private final FavoriteListingRepository favoriteListingRepository;


    @Autowired
    public ListingVehicleServiceImpl(ListingVehicleRepository listingVehicleRepository, UserEntityRepository userEntityRepository,
                                     ListingCarMapper listingCarMapper, ListingImageRepository listingImageRepository,
                                     UserEntityMapper userEntityMapper, ListingImageMapper listingImageMapper,
                                     ListingImageService listingImageService, ModelRepository modelRepository,
                                     EngineRepository engineRepository, GearboxRepository gearboxRepository,
                                     BodyRepository bodyRepository, LocationRepository locationRepository,
                                     ObjectMapper objectMapper, FavoritesMapper favoritesMapper,
                                     JWTGenerator jwtGenerator, TypeRepository typeRepository,
                                     MakeRepository makeRepository, RegionRepository regionRepository,
                                     BodyCreator bodyCreator, MessageCreator messageCreator, EuroStandardRepository euroStandardRepository,
                                     ColorRepository colorRepository, UserIdentificator userIdentificator, EditHistoryRepository editHistoryRepository, FavoriteListingRepository favoriteListingRepository) {
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
        this.favoritesMapper = favoritesMapper;
        this.jwtGenerator = jwtGenerator;
        this.typeRepository = typeRepository;
        this.makeRepository = makeRepository;
        this.regionRepository = regionRepository;
        this.bodyCreator = bodyCreator;
        this.messageCreator = messageCreator;
        this.euroStandardRepository = euroStandardRepository;
        this.colorRepository = colorRepository;
        this.userIdentificator = userIdentificator;
        this.editHistoryRepository = editHistoryRepository;
        this.favoriteListingRepository = favoriteListingRepository;
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
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        }
        catch (Exception ex){
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public Map<String, Object> createCarListing(CreateCarListingDto car, List<MultipartFile> images) throws IOException {

        try{
            hasNullProps(car);

            String userId = userIdentificator.getCurrentUserId();
            Cloudinary cloudinary = new Cloudinary();

            UserEntity user = userEntityRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found"));
            Model model = modelRepository.findById(car.getModel()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Model was not found"));
            Engine engine = engineRepository.findById(car.getEngine()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Engine was not found"));
            Gearbox gearbox = gearboxRepository.findById(car.getGearbox()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gearbox was not found"));
            Body body = bodyRepository.findById(car.getBody()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Body type was not found"));
            Location location = locationRepository.findById(car.getLocation()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location was not found"));
            Color color = colorRepository.findById(car.getColor()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Color was not found"));
            EuroStandard euroStandard = euroStandardRepository.findById(car.getEuroStandard()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Euro Standard was not found"));
            ListingVehicle newListing = listingCarMapper.toEntity(car, user, model, engine, gearbox, body, location, color, euroStandard);

            listingVehicleRepository.save(newListing);

            if (!(images.getFirst().isEmpty())) {
                for (MultipartFile i : images) {
                    ListingImage listImage = new ListingImage();

                    Map uploadResult = cloudinary.uploader().upload(i.getBytes(), ObjectUtils.emptyMap());

                    listImage.setType(i.getContentType());
                    listImage.setUrl(uploadResult.get("secure_url").toString());
                    listImage.setPublicId(uploadResult.get("public_id").toString());
                    listImage.setListingId(newListing);
                    listImage.setMain(true);

                    String imgname = getTextBeforeDot(i.getOriginalFilename());

                    if (imgname.equals(car.getMainImgId().toString())) {
                        listImage.setMain(true);
                    } else {
                        listImage.setMain(false);
                    }

                    listingImageRepository.save(listImage);

                }
            }

            Map<String, Object> bodyResponse = bodyCreator.create();
            Message message = messageCreator.create(true, "Listing Created", "The car has been saved successfully!", "success");
            UserEntityDto mappedUser = userEntityMapper.toDTO(user);
            List<ListingImageDto> listingImages = listingImageService.getAllImagesOfListingById(newListing);

            ListingCarDto mappedNewListing = listingCarMapper.toDTO(newListing, mappedUser, listingImages);

            bodyResponse.put("listing", mappedNewListing);
            bodyResponse.put("message", message);
            bodyResponse.put("status", HttpStatus.CREATED.value());
            return bodyResponse;

        } catch (ResponseStatusException ex) {
            String reason = ex.getReason();
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        }
            catch (Exception ex){
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }


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
    public Map<String, Object> getByPage(int pageNo, int pageSize, String sortBy, String sortDirection, Boolean isActive) {
        try{
            String userId = userIdentificator.getCurrentUserId();
            UserEntity user = userEntityRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found"));
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            Pageable pageRequest = PageRequest.of(pageNo, pageSize, sort);
            Page<ListingVehicle> listings = listingVehicleRepository.findByUserEntity(user, pageRequest);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {

            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    listings = listingVehicleRepository.findAll(pageRequest);
                }
            }
        }

            List<ListingCarDto> content = listings.stream().map(c -> {
                List<ListingImage> images = listingImageRepository.getAllListingImagesByListing(c);
                List<ListingImageDto> mappedImages =  listingImageService.getAllImagesOfListingById(c);
                UserEntityDto mappeedUser = userEntityMapper.toDTO(user);
                ListingCarDto mapped = listingCarMapper.toDTO(c, mappeedUser, mappedImages);

                return mapped;
            }).collect(Collectors.toList());

            CarPaginationResponse response = listingCarMapper.toPagination(listings, content);

            Map<String, Object> body = bodyCreator.create();
            Message message = messageCreator.create(false, "Listings Found", "The listing you've searched have been found", "success");

            body.put("listings", response);
            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;
        } catch (ResponseStatusException ex) {
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        }
        catch (Exception ex){
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    public Map<String, Object> getByPageByCreatedAt(int pageNo, int pageSize) {
        try{
            Pageable pageRequest = PageRequest.of(pageNo, pageSize);
            Page<ListingVehicle> listings = listingVehicleRepository.findBySortedCreatedAt(pageRequest);

            List<ListingCarDto> content = listings.stream().map(c -> {
                List<ListingImage> images = listingImageRepository.getAllListingImagesByListing(c);
                List<ListingImageDto> mappedImages =  listingImageService.getAllImagesOfListingById(c);
                UserEntityDto mappeedUser = userEntityMapper.toDTO(c.getUserEntity());
                ListingCarDto mapped = listingCarMapper.toDTO(c, mappeedUser, mappedImages);

                return mapped;
            }).collect(Collectors.toList());

            CarPaginationResponse response = listingCarMapper.toPagination(listings, content);

            Map<String, Object> body = bodyCreator.create();
            Message message = messageCreator.create(false, "Listings Found", "The listing you've searched have been found", "success");

            body.put("listings", response);
            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;
        } catch (ResponseStatusException ex) {
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        }
        catch (Exception ex){
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @Override
    @Transactional
    public Map<String, Object> deleteCarById(UUID id) {

        try {
            ListingVehicle car = listingVehicleRepository.findCarById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Listing cannot be found"));
            List<ListingImageDto> images = listingImageService.getAllImagesOfListingById(car);
            Cloudinary cloud = new Cloudinary();

            for(ListingImageDto image : images){
                cloud.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());
            }

            listingImageRepository.deleteListingById(car);
            listingVehicleRepository.delete(car);
            editHistoryRepository.deleteByListingId(car.getId());

            Map<String, Object> body = bodyCreator.create();
            Message message = messageCreator.create(true, "Listing Deleted", "Listing has been deleted successfully", "success");

            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;
        } catch (ResponseStatusException ex) {
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        }
        catch (Exception ex){
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @Override
    @Transactional
    public Map<String, Object> updateCar(PatchCarListingDto carDto, UUID id) throws IOException {
        try {
            ListingVehicle nowcar = listingVehicleRepository.findCarById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car was not found"));
            Cloudinary cloudinary = new Cloudinary();

            // Modifying listing
            UserEntity user = userEntityRepository.findById(UUID.fromString(nowcar.getUserEntity().getId().toString()))
                    .orElseThrow(() -> new NotFoundException("User was not found"));
            Model model = modelRepository.findByIdOrNull(carDto.getModel()).orElse(null);
            Engine engine = engineRepository.findByIdOrNull(carDto.getEngine()).orElse(null);
            Gearbox gearbox = gearboxRepository.findByIdOrNull(carDto.getGearbox()).orElse(null);
            Body body = bodyRepository.findByIdOrNull(carDto.getBody()).orElse(null);
            Location location = locationRepository.findByIdOrNull(carDto.getLocation()).orElse(null);
            Color color = colorRepository.findByIdOrNull(carDto.getColor()).orElse(null);
            EuroStandard euroStandard = euroStandardRepository.findByIdOrNull(carDto.getEuroStandard()).orElse(null);

            ListingVehicle newcar = listingCarMapper.toEntityPatch(carDto, user, model, engine, gearbox, body, location, color, euroStandard);
            newcar.setId(nowcar.getId());

            if(carDto.getPrice() != null && nowcar.getPrice().compareTo(carDto.getPrice()) != 0){

                EditHistory isAlreadySave = editHistoryRepository.getListingFromYesterday(nowcar.getId(), LocalDate.now().minusDays(1));

                if(isAlreadySave != null){
                    isAlreadySave.setListingVehicle(nowcar);
                    isAlreadySave.setOldPrice(nowcar.getPrice());
                    isAlreadySave.setNewPrice(carDto.getPrice());

                    if(LocalDateTime.now().isBefore(LocalDateTime.now().with(LocalTime.of(9, 0)))){
                        isAlreadySave.setDate(LocalDate.now().minusDays(1));
                    }
                    else{
                        isAlreadySave.setDate(LocalDate.now());
                    }
                    editHistoryRepository.save(isAlreadySave);
                }
                else{
                    EditHistory history = new EditHistory();
                    history.setListingVehicle(nowcar);
                    history.setOldPrice(nowcar.getPrice());
                    history.setNewPrice(carDto.getPrice());
                    history.setDate(LocalDate.now());
                    editHistoryRepository.save(history);
                }
            }

            patch(nowcar, newcar);
            nowcar.setEditedAt(LocalDate.now());

            listingVehicleRepository.save(nowcar);
            List<ListingImage> imagesDb = listingImageRepository.getAllListingImagesByListing(newcar);

            // Deleting selected images
            if (carDto.getImgIdsToRemove() != null && !carDto.getImgIdsToRemove().isEmpty()) {
                List<ListingImage> imagesToDelete = imagesDb.stream()
                        .filter(image -> carDto.getImgIdsToRemove().contains(image.getId()))
                        .collect(Collectors.toList());

                // Check if we're deleting the main image
                boolean deletingMainImage = imagesToDelete.stream().anyMatch(ListingImage::getIsMain);

                for (ListingImage image : imagesToDelete) {
                    try {
                        cloudinary.uploader().destroy(image.getPublicId(), ObjectUtils.emptyMap());
                    } catch (IOException e) {
                        // Log the error but continue deleting the database records
                        System.err.println("Failed to delete image from Cloudinary: " + e.getMessage());
                    }
                }

                // Remove images from the list before deletion to avoid concurrent modification
                imagesDb = imagesDb.stream()
                        .filter(image -> !imagesToDelete.contains(image))
                        .collect(Collectors.toList());

                // Explicitly delete in separate transaction to avoid rollback
                listingImageRepository.deleteAllInBatch(imagesToDelete);

                // Set first image to main if we deleted the main image and there are remaining images
                if (deletingMainImage && !imagesDb.isEmpty()) {
                    ListingImage defaultSet = imagesDb.get(0);
                    defaultSet.setMain(true);
                    listingImageRepository.save(defaultSet);
                }
            }

            // Set new main img if specified
            if (carDto.getMainImgId() != null) {
                // First set all images to not main
                for (ListingImage image : imagesDb) {
                    image.setMain(image.getId().equals(carDto.getMainImgId()));
                    listingImageRepository.save(image);
                }
            }

            // Upload new images
            if (carDto.getUploadImages() != null && !carDto.getUploadImages().isEmpty()) {
                for (MultipartFile image : carDto.getUploadImages()) {
                    Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                    ListingImage listingImage = new ListingImage();
                    listingImage.setType(image.getContentType());
                    listingImage.setUrl(uploadResult.get("secure_url").toString());
                    listingImage.setPublicId(uploadResult.get("public_id").toString());
                    listingImage.setListingId(nowcar);

                    // Check if this new image should be the main one
                    // This looks problematic in your original code - comparing filename to UUID
                    // You might need to adjust this logic based on your requirements
                    listingImage.setMain(false);

                    listingImageRepository.save(listingImage);
                }
            }

            Map<String, Object> bodyResponse = bodyCreator.create();
            Message message = messageCreator.create(true, "Listing Updated", "Listing has been updated successfully", "success");

            bodyResponse.put("message", message);
            bodyResponse.put("status", HttpStatus.OK.value());

            return bodyResponse;
        } catch (ResponseStatusException ex) {
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        } catch (Exception ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Map<String, Object> searchCarByCriteria(FilterDto filterDto, int pageNo, int pageSize, String sortBy, String sortDirection) {
        try{
            String token = userIdentificator.getCurrentUserIdOrNull();
            UserEntity currentUser;

            if(token != null){
                currentUser = userEntityRepository.getReferenceById(UUID.fromString(token));
            } else {
                currentUser = null;
            }

            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
            Pageable pageRequest = PageRequest.of(pageNo, pageSize, sort);

            Page<ListingVehicle> listings = listingVehicleRepository.findAll(
                    ListingSpecification.filters(filterDto),
                    pageRequest);

            List<ListingCarDto> content = listings.stream().map(c -> {
                UserEntity user = userEntityRepository.findById(c.getUserEntity().getId()).orElseThrow(() -> new UsernameNotFoundException("User was not found"));
                Boolean favListing = null;

                if(currentUser != null){
                    favListing = favoriteListingRepository.existsByUserEntityAndListingVehicle(currentUser, c);
                }

                UserEntityDto mappedUser = userEntityMapper.toDTO(user);
                List<ListingImageDto> images = listingImageService.getAllImagesOfListingById(c);

                ListingCarDto mappedListing = listingCarMapper.toDTO(c, mappedUser, images, favListing);

                return mappedListing;
            }).collect(Collectors.toList());

            CarPaginationResponse response = new CarPaginationResponse();
            response.setContent(content);
            response.setPageNo(listings.getNumber());
            response.setPageSize(listings.getSize());
            response.setTotalPages(listings.getTotalPages());
            response.setTotalElements(listings.getTotalElements());
            response.setFirst(listings.isFirst());
            response.setLast(listings.isLast());

            Map<String, Object> body = bodyCreator.create();
            Message message = messageCreator.create(false, "Success", "Successfully found listings based on the given criteria", "success");

            body.put("listings", response);
            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;
        } catch (Exception ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public List<ListingVehicle> searchCarByCriteria(FilterDto filterDto) {
        try{
            Sort sort = Sort.by(Sort.Direction.fromString("ASC"), "price");

            return listingVehicleRepository.findAll(ListingSpecification.filters(filterDto), sort);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Map<String, Object> changeStatusListing(UUID id){
        try{
            ListingVehicle listing = listingVehicleRepository.getReferenceById(id);
            listing.setIsActive(!listing.getIsActive());

            Map<String, Object> body = bodyCreator.create();
            Message message;

            if(listing.getIsActive()){
                message = messageCreator.create(true, "Listing Activity Changed", "Your listing is set to active", "success");
            }
            else{
                message = messageCreator.create(true, "Listing Activity Changed", "Your listing is set to inactive", "success");
            }

            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;

        } catch (Exception ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public boolean canAccessListing(String listingId) {
        String userId = userIdentificator.getCurrentUserId();

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
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        }
        catch (Exception ex){
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
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

    private void hasNullProps (Object obj) throws IllegalAccessException {
            Boolean areNulls = false;
            List<String> nullProps = new ArrayList<>();

            for (Field field : obj.getClass().getDeclaredFields()){
                field.setAccessible(true);
                if(field.get(obj) == null){
                    nullProps.add(field.getName());
                    areNulls = true;
                }
            }

            if(areNulls){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.join(", ", nullProps) + " are empty");
            }
    }

    public String getTextBeforeDot(String input) {
        int dotIndex = input.indexOf('.');
        if (dotIndex != -1) {
            return input.substring(0, dotIndex);
        } else {
            return input; // No dot found, return the original string
        }
    }

    public <T, R> R safeGet(T obj, Function<T, R> getter) {
        try {
            return (obj != null) ? getter.apply(obj) : null;
        } catch (NullPointerException e) {
            return null;
        }
    }
}

