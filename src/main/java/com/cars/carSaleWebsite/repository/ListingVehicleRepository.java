package com.cars.carSaleWebsite.repository;

import com.cars.carSaleWebsite.helpers.ListingSpecification;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ListingVehicleRepository extends JpaRepository<ListingVehicle, UUID>, JpaSpecificationExecutor<ListingVehicle> {
//    @Query("Select l from listingVehicle l" +
//            "JOIN body b ON l.bodyId = b.id" +
//            "JOIN type t ON b.typeId = t.id " +
//            "WHERE t.type = 'Car' AND l.id = :id")
    @Query("SELECT l FROM ListingVehicle l " +
        "JOIN l.body b " +
        "JOIN b.type t " +
        "WHERE t.type = 'Car' AND l.id = :id")
    Optional<ListingVehicle> findCarById(@Param("id") UUID id);

    @Query("SELECT l FROM ListingVehicle l " +
            "JOIN l.body b " +
            "JOIN b.type t " +
            "WHERE t.type = 'Car'")
    HashSet<ListingVehicle> getAllCars();

   Page<ListingVehicle> findAllByUserEntity(UserEntity userEntity, Pageable pageable);

}
