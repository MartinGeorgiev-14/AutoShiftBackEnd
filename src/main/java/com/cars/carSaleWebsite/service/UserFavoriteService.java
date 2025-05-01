package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.dto.Listing.CRUD.FilterDto;

import java.util.Map;
import java.util.UUID;

public interface UserFavoriteService {
    Map<String, Object> getAllFavoriteListings(int pageNo, int pageSize, String sortBy, String sortDirection);
    Map<String, Object> addFavoriteListingToUser(UUID listingId);
    Map<String, Object> removeFavoriteListingFromUser(UUID listingId);
    Map<String, Object> changeFavoriteListingNotify(UUID id);
    Map<String, Object> getAllFavoriteFilters(int pageNo, int pageSize);
    Map<String, Object> addFavoriteFilterToUser(FilterDto filter);
    Map<String, Object> removeFavoriteFilterFromUser(UUID id);
    Map<String, Object> changeFavoriteFilterName(UUID id, String name);
    Map<String, Object> changeFavoriteFilterNotify(UUID id);
}
