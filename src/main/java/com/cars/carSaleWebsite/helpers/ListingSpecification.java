package com.cars.carSaleWebsite.helpers;

import com.cars.carSaleWebsite.dto.Listing.FilterDto;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.vehicle.Color;
import org.springframework.boot.autoconfigure.rsocket.RSocketProperties;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Component
public class ListingSpecification {

    public static Specification<ListingVehicle> filters(FilterDto filter) {
        return Specification.where(
                byMake(filter.getMake())
                        .and(byModel(filter.getModel()))
                        .and(byType(filter.getType()))
                        .and(byBody(filter.getBody()))
                        .and(byUser(filter.getUserId()))
                        .and(byRegion(filter.getRegion()))
                        .and(byLocation(filter.getLocation()))
                        .and(byGearbox(filter.getGearbox()))
                        .and(byEngine(filter.getEngine()))
                        .and(byPriceRange(filter.getPriceStart(), filter.getPriceEnd()))
                        .and(byColor(filter.getColor()))
                        .and(byEuroStandard(filter.getEuroStandard()))
                        .and(byDateRange(filter.getDateStart(), filter.getDateEnd()))
                        .and(byHorsepowerRange(filter.getHorsepowerStart(), filter.getHorsepowerEnd()))
                        .and(byMileageRange(filter.getMileageStart(), filter.getMileageEnd()))
                        .and(byEngineDisplacementRange(filter.getEngineDisplacementStart(), filter.getEngineDisplacementEnd())));
    }

    public static Specification<ListingVehicle> filterForUser(UUID id){
        return (root, query, criteriaBuilder) ->
            id != null ? criteriaBuilder.equal(root.get("userEntity").get("id"), id) : criteriaBuilder.conjunction();
        
    }

    private static Specification<ListingVehicle> byMake(UUID make) {
        return (root, query, criteriaBuilder) ->
                make != null ? criteriaBuilder.equal(root.get("model").get("make").get("id"), make) : criteriaBuilder.conjunction();
    }

    private static Specification<ListingVehicle> byModel(UUID model) {
        return (root, query, criteriaBuilder) ->
                model != null ? criteriaBuilder.equal(root.get("model").get("id"), model) : criteriaBuilder.conjunction();
    }

    private static Specification<ListingVehicle> byType(UUID type) {
        return (root, query, criteriaBuilder) ->
                type != null ? criteriaBuilder.equal(root.get("body").get("type").get("id"), type) : criteriaBuilder.conjunction();
    }

    private static Specification<ListingVehicle> byBody(UUID body) {
        return (root, query, criteriaBuilder) ->
                body != null ? criteriaBuilder.equal(root.get("body").get("id"), body) : criteriaBuilder.conjunction();
    }

    private static Specification<ListingVehicle> byUser(UUID user) {
        return (root, query, criteriaBuilder) ->
                user != null ? criteriaBuilder.equal(root.get("userEntity").get("id"), user) : criteriaBuilder.conjunction();
    }

    private static Specification<ListingVehicle> byRegion(UUID region) {
        return (root, query, criteriaBuilder) ->
                region != null ? criteriaBuilder.equal(root.get("location").get("region").get("id"), region) : criteriaBuilder.conjunction();
    }

    private static Specification<ListingVehicle> byLocation(UUID location) {
        return (root, query, criteriaBuilder) ->
                location != null ? criteriaBuilder.equal(root.get("location").get("id"), location) : criteriaBuilder.conjunction();
    }

    private static Specification<ListingVehicle> byGearbox(UUID gearbox) {
        return (root, query, criteriaBuilder) ->
                gearbox != null ? criteriaBuilder.equal(root.get("gearbox").get("id"), gearbox) : criteriaBuilder.conjunction();
    }

    private static Specification<ListingVehicle> byEngine(UUID engine) {
        return (root, query, criteriaBuilder) ->
                engine != null ? criteriaBuilder.equal(root.get("engine").get("id"), engine) : criteriaBuilder.conjunction();
    }


    private static Specification<ListingVehicle> byPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {

            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            }
            if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            }
            if (maxPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            return criteriaBuilder.conjunction();
        };
    }

    private static Specification<ListingVehicle> byDateRange(Date minDate, Date maxDate) {
        return (root, query, criteriaBuilder) -> {

            if (minDate != null && maxDate != null) {
                return criteriaBuilder.between(root.get("manufactureDate"), minDate, maxDate);
            }
            if (minDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("manufactureDate"), minDate);
            }
            if (maxDate != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("manufactureDate"), maxDate);
            }
            return criteriaBuilder.conjunction();
        };
    }

    private static Specification<ListingVehicle> byHorsepowerRange(Integer minHorsepower, Integer maxHorsepower) {
        return (root, query, criteriaBuilder) -> {

            if (minHorsepower != null && maxHorsepower != null) {
                return criteriaBuilder.between(root.get("horsepower"), minHorsepower, maxHorsepower);
            }
            if (minHorsepower != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("horsepower"), minHorsepower);
            }
            if (maxHorsepower != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("horsepower"), maxHorsepower);
            }
            return criteriaBuilder.conjunction();
        };
    }

    private static Specification<ListingVehicle> byMileageRange(Integer minMileage, Integer maxMileage) {
        return (root, query, criteriaBuilder) -> {

            if (minMileage != null && maxMileage != null) {
                return criteriaBuilder.between(root.get("mileage"), minMileage, maxMileage);
            }
            if (minMileage != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("mileage"), minMileage);
            }
            if (maxMileage != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("mileage"), maxMileage);
            }
            return criteriaBuilder.conjunction();
        };
    }

    private static Specification<ListingVehicle> byEngineDisplacementRange(Integer minEngineDisplacement, Integer maxEngineDisplacement) {
        return (root, query, criteriaBuilder) -> {

            if (minEngineDisplacement != null && maxEngineDisplacement != null) {
                return criteriaBuilder.between(root.get("engineDisplacement"), minEngineDisplacement, maxEngineDisplacement);
            }
            if (minEngineDisplacement != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("engineDisplacement"), minEngineDisplacement);
            }
            if (maxEngineDisplacement != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("engineDisplacement"), maxEngineDisplacement);
            }
            return criteriaBuilder.conjunction();
        };
    }

    private static Specification<ListingVehicle> byColor(UUID color){
        return (root, query, criteriaBuilder) ->
                color != null ? criteriaBuilder.equal(root.get("color").get("id"), color) : criteriaBuilder.conjunction();
    }

    private static Specification<ListingVehicle> byEuroStandard(UUID standard){
        return (root, query, criteriaBuilder) ->
                standard != null ? criteriaBuilder.equal(root.get("standard").get("id"), standard) : criteriaBuilder.conjunction();
    }

}