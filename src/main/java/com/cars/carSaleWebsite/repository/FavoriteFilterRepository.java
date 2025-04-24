package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.userFavorites.FavoriteFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface FavoriteFilterRepository extends JpaRepository<FavoriteFilter, UUID> {
//    @Query("SELECT f.listingVehicle FROM FavoriteListing f WHERE f.userEntity = :user")
//    @Param("user")
    Page<FavoriteFilter> findFavoriteFiltersByUserEntity(UserEntity user, Pageable pageable);
}
