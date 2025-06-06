package com.cars.carSaleWebsite.repository.userFavorite;

import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.userFavorites.FavoriteListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteListingRepository extends JpaRepository<FavoriteListing, UUID> {
    @Query("SELECT f.listingVehicle FROM FavoriteListing f WHERE f.userEntity = :user")
    Page<ListingVehicle> findFavoriteListingsByUser(@Param("user") UserEntity user, Pageable pageable);

    @Query("SELECT f FROM FavoriteListing f WHERE f.userEntity = :user")
    List<FavoriteListing> findFavoriteListingsByUser(@Param("user") UserEntity user);

    @Query("SELECT f FROM FavoriteListing f WHERE f.userEntity = :user AND f.listingVehicle = :listing")
    FavoriteListing findFavoriteListingByListing(@Param("user") UserEntity user, @Param("listing") ListingVehicle listing);

    Optional<FavoriteListing> findByUserEntityAndListingVehicle(UserEntity userEntity, ListingVehicle listingVehicle);

    @Query("SELECT COUNT(f) > 0 FROM FavoriteListing f WHERE f.userEntity = :user AND f.listingVehicle = :listing")
    Boolean existsByUserEntityAndListingVehicle(@Param("user") UserEntity user, @Param("listing") ListingVehicle listing);

}
