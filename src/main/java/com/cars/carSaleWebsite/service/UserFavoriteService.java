package com.cars.carSaleWebsite.service;

import java.util.Map;
import java.util.UUID;

public interface UserFavoriteService {
    Map<String, Object> getAllFavoriteListings(int pageNo, int pageSize, String sortBy, String sortDirection);
    Map<String, Object> addFavoriteListingToUser(UUID listingId);
    Map<String, Object> removeFavoriteListingFromUser(UUID listingId);
    Map<String, Object> getAllFavoriteFilters(int pageNo, int pageSize);
}
