package com.cars.carSaleWebsite.service.impl;

import com.cars.carSaleWebsite.helpers.MessageCreator;
import com.cars.carSaleWebsite.helpers.UserIdentificator;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.userFavorites.FavoriteFilter;
import com.cars.carSaleWebsite.repository.FavoriteFilterRepository;
import com.cars.carSaleWebsite.repository.ListingVehicleRepository;
import com.cars.carSaleWebsite.repository.UserEntityRepository;
import com.cars.carSaleWebsite.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

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
