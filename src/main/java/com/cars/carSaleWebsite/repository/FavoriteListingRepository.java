package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.userFavorites.FavoriteListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteListingRepository extends JpaRepository<FavoriteListing, UUID> {
    @Query("SELECT f.listingVehicle FROM FavoriteListing f WHERE f.userEntity = :user")
    Page<ListingVehicle> findFavoriteListingsByUser(@Param("user") UserEntity user, Pageable pageable);

    Optional<FavoriteListing> findByUserEntityAndListingVehicle(UserEntity userEntity, ListingVehicle listingVehicle);
}
