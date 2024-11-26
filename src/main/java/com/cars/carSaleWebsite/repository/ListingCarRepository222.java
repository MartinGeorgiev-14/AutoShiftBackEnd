//package com.cars.carSaleWebsite.repository;
//
//import com.cars.carSaleWebsite.models.entities.listing.ListingCar;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.UUID;
//
//@Repository
//public interface ListingCarRepository extends JpaRepository<ListingCar, UUID> {
//    @Query("SELECT c FROM ListingCar c")
//    HashSet<ListingCar> getAllSet();
//}
