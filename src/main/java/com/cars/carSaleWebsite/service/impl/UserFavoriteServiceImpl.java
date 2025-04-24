package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.dto.Authentication.UserEntityDto;
import com.cars.carSaleWebsite.dto.Listing.*;
import com.cars.carSaleWebsite.helpers.BodyCreator;
import com.cars.carSaleWebsite.helpers.MessageCreator;
import com.cars.carSaleWebsite.helpers.UserIdentificator;
import com.cars.carSaleWebsite.mappers.ListingCarMapper;
import com.cars.carSaleWebsite.mappers.UserEntityMapper;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.userFavorites.FavoriteFilter;
import com.cars.carSaleWebsite.models.entities.userFavorites.FavoriteListing;
import com.cars.carSaleWebsite.models.nonEntity.Message;
import com.cars.carSaleWebsite.repository.*;
import com.cars.carSaleWebsite.service.ListingImageService;
import com.cars.carSaleWebsite.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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
    private MessageCreator messageCreator;

    @Autowired
    public UserFavoriteServiceImpl(FavoriteFilterRepository favoriteFilterRepository,
                                   FavoriteListingRepository favoriteListingRepository, UserIdentificator userIdentificator,
                                   BodyCreator bodyCreator, UserEntityRepository userEntityRepository,
                                   ListingImageRepository listingImageRepository, ListingVehicleRepository listingVehicleRepository,
                                   ListingImageService listingImageService, UserEntityMapper userEntityMapper,
                                   ListingCarMapper listingCarMapper, MessageCreator messageCreator) {

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
    }

    public Map<String, Object> getAllFavoriteListings(int pageNo, int pageSize, String sortBy, String sortDirection) {
        try {
            String userId = userIdentificator.getCurrentUserId();
            UserEntity user = userEntityRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found"));
            Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "listingVehicle." + sortBy);
            Pageable pageRequest = PageRequest.of(pageNo, pageSize, sort);
            Page<ListingVehicle> listings = favoriteListingRepository.findFavoriteListingsByUser(user, pageRequest);

            List<ListingCarDto> content = listings.stream().map(c -> {
                List<ListingImageDto> mappedImages = listingImageService.getAllImagesOfListingById(c);
                UserEntityDto mappeedUser = userEntityMapper.toDTO(user);
                ListingCarDto mapped = listingCarMapper.toDTO(c, mappeedUser, mappedImages);

                return mapped;
            }).collect(Collectors.toList());

            CarPaginationResponse response = listingCarMapper.toPagination(listings, content);

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

    public Map<String, Object> getAllFavoriteFilters(int pageNo, int pageSize) {
        try {
            String userId = userIdentificator.getCurrentUserId();
            UserEntity user = userEntityRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found"));
            Sort sort = Sort.by(Sort.Direction.ASC, "model.make");
            Pageable pageRequest = PageRequest.of(pageNo, pageSize, sort);
            Page<FavoriteFilter> listings = favoriteFilterRepository.findFavoriteFiltersByUserEntity(user, pageRequest);

            List<FavoriteFilterDto> content = listings.stream().map(f -> {
                return listingCarMapper.toFavoriteFilterDto(f);
            }).collect(Collectors.toList());

            FilterPaginationResponse response = listingCarMapper.toFilterPagination(listings, content);

            Map<String, Object> body = bodyCreator.create();
            Message message = messageCreator.create(false, "Favorite Filters Found", "The favorite filters have been found", "success");

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
}
