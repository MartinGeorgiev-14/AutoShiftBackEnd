package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.Authentication.UserEntityDto;
import com.cars.carSaleWebsite.dto.Listing.*;
import com.cars.carSaleWebsite.dto.Listing.CRUD.FilterDto;
import com.cars.carSaleWebsite.dto.UserFavorites.*;
import com.cars.carSaleWebsite.helpers.BodyCreator;
import com.cars.carSaleWebsite.helpers.MessageCreator;
import com.cars.carSaleWebsite.helpers.UserIdentificator;
import com.cars.carSaleWebsite.mappers.FavoritesMapper;
import com.cars.carSaleWebsite.mappers.ListingCarMapper;
import com.cars.carSaleWebsite.mappers.UserEntityMapper;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.userFavorites.FavoriteFilter;
import com.cars.carSaleWebsite.models.entities.userFavorites.FavoriteListing;
import com.cars.carSaleWebsite.models.nonEntity.Message;
import com.cars.carSaleWebsite.repository.listing.ListingImageRepository;
import com.cars.carSaleWebsite.repository.listing.ListingVehicleRepository;
import com.cars.carSaleWebsite.repository.userFavorite.FavoriteFilterRepository;
import com.cars.carSaleWebsite.repository.userFavorite.FavoriteListingRepository;
import com.cars.carSaleWebsite.repository.user.UserEntityRepository;
import com.cars.carSaleWebsite.service.ListingImageService;
import com.cars.carSaleWebsite.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

@Service("userFavoriteService")
public class UserFavoriteServiceImpl implements UserFavoriteService {

    private final FavoriteFilterRepository favoriteFilterRepository;
    private final FavoriteListingRepository favoriteListingRepository;
    private UserIdentificator userIdentificator;
    private final BodyCreator bodyCreator;
    private UserEntityRepository userEntityRepository;
    private ListingImageRepository listingImageRepository;
    private ListingVehicleRepository listingVehicleRepository;
    private final ListingImageService listingImageService;
    private final UserEntityMapper userEntityMapper;
    private ListingCarMapper listingCarMapper;
    private FavoritesMapper favoritesMapper;
    private MessageCreator messageCreator;

    @Autowired
    public UserFavoriteServiceImpl(FavoriteFilterRepository favoriteFilterRepository,
                                   FavoriteListingRepository favoriteListingRepository, UserIdentificator userIdentificator,
                                   BodyCreator bodyCreator, UserEntityRepository userEntityRepository,
                                   ListingImageRepository listingImageRepository, ListingVehicleRepository listingVehicleRepository,
                                   ListingImageService listingImageService, UserEntityMapper userEntityMapper,
                                   ListingCarMapper listingCarMapper, MessageCreator messageCreator,
                                   FavoritesMapper favoritesMapper) {

        this.favoriteFilterRepository = favoriteFilterRepository;
        this.favoriteListingRepository = favoriteListingRepository;
        this.userIdentificator = userIdentificator;
        this.bodyCreator = bodyCreator;
        this.userIdentificator = userIdentificator;
        this.listingImageService = listingImageService;
        this.userEntityMapper = userEntityMapper;
        this.listingCarMapper = listingCarMapper;
        this.messageCreator = messageCreator;
        this.userEntityRepository = userEntityRepository;
        this.listingVehicleRepository = listingVehicleRepository;
        this.listingImageRepository = listingImageRepository;
        this.favoritesMapper = favoritesMapper;
    }

    public Map<String, Object> getAllFavoriteListings(int pageNo, int pageSize, String sortBy, String sortDirection) {
        try {
            String userId = userIdentificator.getCurrentUserId();
            UserEntity user = userEntityRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found"));
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "listingVehicle." + sortBy);
            Pageable pageRequest = PageRequest.of(pageNo, pageSize, sort);
            Page<ListingVehicle> listings = favoriteListingRepository.findFavoriteListingsByUser(user, pageRequest);

            List<FavoriteListingDto> content = listings.stream().map(c -> {
                List<ListingImageDto> mappedImages = listingImageService.getAllImagesOfListingById(c);
                UserEntityDto mappeedUser = userEntityMapper.toDTO(user);
                FavoriteListing fav = favoriteListingRepository.findFavoriteListingByListing(user, c);
                FavoriteListingDto mapped = favoritesMapper.toFavoriteListingDto(c, mappeedUser, mappedImages, fav);

                return mapped;
            }).collect(Collectors.toList());

            FavoriteListingPaginationResponseDto response = favoritesMapper.toFavoriteListingPagination(listings, content);

            Map<String, Object> body = bodyCreator.create();
            Message message = messageCreator.create(false, "Favorite Listings Found", "The favorite listings you've searched have been found", "success");

            body.put("listings", response);
            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;
        } catch (ResponseStatusException ex) {
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        } catch (Exception ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public Map<String, Object> addFavoriteListingToUser(UUID listingId) {
        try {
            String userId = userIdentificator.getCurrentUserId();
            UserEntity user = userEntityRepository.getReferenceById(UUID.fromString(userId));
            ListingVehicle listing = listingVehicleRepository.getReferenceById(listingId);
            Optional<FavoriteListing> existingFavorite = favoriteListingRepository.findByUserEntityAndListingVehicle(user, listing);

            Message message = new Message();
            HttpStatus status;

            if(existingFavorite.isEmpty()){
                FavoriteListing newFavorite = new FavoriteListing();
                newFavorite.setUserEntity(user);
                newFavorite.setListingVehicle(listing);
                newFavorite.setIsNotify(true);
                favoriteListingRepository.save(newFavorite);
                message = messageCreator.reassignProps(message,true, "Added to Favorites", "The listing has been successfully added to your favorites", "success");
                status = HttpStatus.OK;
            }
            else{
                message = messageCreator.reassignProps(message,true, "Cannot add to Favorite", "The listing has been added to favorites already", "error");
                status = HttpStatus.CONFLICT;
            }

            Map<String, Object> body = bodyCreator.create();
            body.put("message", message);
            body.put("status", status.value());

            return body;
        } catch (ResponseStatusException ex) {
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        } catch (Exception ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Map<String, Object> removeFavoriteListingFromUser(UUID listingId){
        try{
            String userId = userIdentificator.getCurrentUserId();
            UserEntity user = userEntityRepository.getReferenceById(UUID.fromString(userId));
            ListingVehicle listing = listingVehicleRepository.getReferenceById(listingId);
            Optional<FavoriteListing> existingFavorite = favoriteListingRepository.findByUserEntityAndListingVehicle(user, listing);

            Message message = new Message();
            HttpStatus status;

            if(existingFavorite.isPresent()){
                FavoriteListing toDelete = favoriteListingRepository.getReferenceById(existingFavorite.get().getId());
                favoriteListingRepository.delete(toDelete);
                message = messageCreator.reassignProps(message,true, "Removed from Favorites", "The listing has been successfully removed from your favorites", "success");
                status = HttpStatus.OK;
            }
            else{
                message = messageCreator.reassignProps(message,true, "Cannot Remove from Favorite", "There is no such listing to be removed from favorite", "error");
                status = HttpStatus.CONFLICT;
            }

            Map<String, Object> body = bodyCreator.create();
            body.put("message", message);
            body.put("status", status.value());

            return body;

        }catch (ResponseStatusException ex) {
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        } catch (Exception ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Map<String, Object> changeFavoriteListingNotify(UUID id){
        try{
            String userId = userIdentificator.getCurrentUserId();
            UserEntity user = userEntityRepository.getReferenceById(UUID.fromString(userId));

            FavoriteListing listing = favoriteListingRepository.getReferenceById(id);
            listing.setIsNotify(!listing.getIsNotify());
            favoriteListingRepository.save(listing);

            if(!listing.getUserEntity().equals(user)){
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner of this filter save");
            }

            Map<String, Object> body = bodyCreator.create();
            Message message;

            if(listing.getIsNotify()){
                message = messageCreator.create(true, "Notify changed", "You will be notified for this filter", "success");
            }
            else{
                message = messageCreator.create(true, "Notify changed", "You will not be notified for this filter", "success");
            }

            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;
        } catch (ResponseStatusException ex) {
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        } catch (Exception ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public Map<String, Object> getAllFavoriteFilters(int pageNo, int pageSize) {
        try {
            String userId = userIdentificator.getCurrentUserId();
            UserEntity user = userEntityRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found"));
            Sort sort = Sort.by(Sort.Direction.ASC, "model.make");
            Pageable pageRequest = PageRequest.of(pageNo, pageSize, sort);
            Page<FavoriteFilter> listings = favoriteFilterRepository.findFavoriteFiltersByUserEntity(user, pageRequest);

            List<FavoriteFilterResponseDto> content = listings.stream().map(f -> {
                return favoritesMapper.toFavoriteFilterDto(f);
            }).collect(Collectors.toList());

            FilterPaginationResponseDto response = favoritesMapper.toFilterPagination(listings, content);

            Map<String, Object> body = bodyCreator.create();
            Message message = messageCreator.create(false, "Favorite Filters Found", "The favorite filters have been found", "success");

            body.put("filters", response);
            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;
        } catch (ResponseStatusException ex) {
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        } catch (Exception ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public Map<String, Object> addFavoriteFilterToUser(FilterDto filter){
        try {
            hasValueProps(filter);

            String userId = userIdentificator.getCurrentUserId();
            UserEntity user = userEntityRepository.getReferenceById(UUID.fromString(userId));

            FavoriteFilter result = favoritesMapper.toEntity(filter, user);

            Example<FavoriteFilter> example = Example.of(result);
            boolean exists = favoriteFilterRepository.exists(example);

            Map<String, Object> body = bodyCreator.create();
            Message message;
            HttpStatus status;

            if(!exists){
                favoriteFilterRepository.save(result);
                message = messageCreator.create(true, "Added to Favorites", "The filter has been added to favorites successfully", "success");
                status = HttpStatus.OK;
            }
            else{
                message = messageCreator.create(true, "Error Adding to Favorites", "Cannot add filter to favorites twice", "error");
                status = HttpStatus.CONFLICT;
            }

            body.put("message", message);
            body.put("status", status.value());

            return body;

        } catch (ResponseStatusException ex) {
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        } catch (Exception ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public Map<String, Object> removeFavoriteFilterFromUser(UUID id){
        try{
            FavoriteFilter filter = checkFilterOwner(id);

            favoriteFilterRepository.delete(filter);

            Map<String, Object> body = bodyCreator.create();
            Message message = messageCreator.create(true, "Filter Removed", "The filter has been removed from favorites", "success");

            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;
        } catch (ResponseStatusException ex) {
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        } catch (Exception ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public Map<String, Object> changeFavoriteFilterName(UUID id, String name){
        try{

            if (name.length() > 1000){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The name is too long");
            }

            FavoriteFilter filter = checkFilterOwner(id);

            filter.setName(name);
            favoriteFilterRepository.save(filter);

            Map<String, Object> body = bodyCreator.create();
            Message message = messageCreator.create(true, "Name Changed", "The name has been changed", "success");

            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;

        }
        catch (ResponseStatusException ex) {
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        } catch (Exception ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional
    public Map<String, Object> changeFavoriteFilterNotify(UUID id){
        try{
            FavoriteFilter filter = checkFilterOwner(id);
            filter.setIsNotify(!filter.getIsNotify());
            favoriteFilterRepository.save(filter);

            Map<String, Object> body = bodyCreator.create();
            Message message;

            if(filter.getIsNotify()){
                message = messageCreator.create(true, "Notify changed", "You will be notified for this filter", "success");
            }
            else{
                message = messageCreator.create(true, "Notify changed", "You will not be notified for this filter", "success");
            }

            body.put("message", message);
            body.put("status", HttpStatus.OK.value());

            return body;

        } catch (ResponseStatusException ex) {
            return messageCreator.createErrorResponse(ex.getReason(), ex.getStatusCode());
        } catch (Exception ex) {
            return messageCreator.createErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private FavoriteFilter checkFilterOwner(UUID id){
        String userId = userIdentificator.getCurrentUserId();
        UserEntity user = userEntityRepository.getReferenceById(UUID.fromString(userId));

        FavoriteFilter filter = favoriteFilterRepository.getReferenceById(id);

        if(!filter.getUserEntity().equals(user)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner of this filter save");
        }

        return filter;
    }

    private void hasValueProps (Object obj) throws IllegalAccessException {
        Boolean areNulls = true;

        for (Field field : obj.getClass().getDeclaredFields()){
            field.setAccessible(true);
            if(field.get(obj) != null){
                areNulls = false;
            }
        }

        if(areNulls){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one field must be filled");
        }
    }
}
