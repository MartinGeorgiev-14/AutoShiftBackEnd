package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.helpers.MessageCreator;
import com.cars.carSaleWebsite.helpers.UserIdentificator;
import com.cars.carSaleWebsite.repository.userFavorite.FavoriteFilterRepository;
import com.cars.carSaleWebsite.repository.listing.ListingVehicleRepository;
import com.cars.carSaleWebsite.repository.user.UserEntityRepository;
import com.cars.carSaleWebsite.service.NotificationService;

public class NotificationServiceImpl implements NotificationService {
    private final FavoriteFilterRepository favoriteFilterRepository;
    private final ListingVehicleRepository listingVehicleRepository;
    private final UserIdentificator userIdentificator;
    private final UserEntityRepository userEntityRepository;

    public NotificationServiceImpl(FavoriteFilterRepository favoriteFilterRepository, ListingVehicleRepository listingVehicleRepository, UserIdentificator userIdentificator, MessageCreator messageCreator, UserEntityRepository userEntityRepository) {
        this.favoriteFilterRepository = favoriteFilterRepository;
        this.listingVehicleRepository = listingVehicleRepository;
        this.userIdentificator = userIdentificator;
        this.userEntityRepository = userEntityRepository;
    }


}
