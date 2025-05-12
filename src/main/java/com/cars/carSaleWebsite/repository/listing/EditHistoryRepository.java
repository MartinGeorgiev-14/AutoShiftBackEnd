package com.cars.carSaleWebsite.repository.listing;

import com.cars.carSaleWebsite.models.entities.listing.EditHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface EditHistoryRepository extends JpaRepository<EditHistory, UUID> {

    @Query("DELETE FROM EditHistory i WHERE i.listingVehicle.id = :id")
    void deleteByListingId(@Param("id") UUID id);

    @Query("SELECT i FROM EditHistory i WHERE i.listingVehicle.id = :id AND i.date = :date")
    EditHistory getListingFromYesterday(@Param("id") UUID id, @Param("date") LocalDate date);

    @Query("SELECT i FROM EditHistory i WHERE i.date = :date")
    List<EditHistory> getListingFromYesterdayList(@Param("date") LocalDate date);
}
