package com.cars.carSaleWebsite.helpers;

import com.cars.carSaleWebsite.dto.FilterDto;
import com.cars.carSaleWebsite.models.entities.listing.ListingVehicle;
import com.cars.carSaleWebsite.models.entities.vehicle.Model;
import jakarta.persistence.criteria.Predicate;
import org.hibernate.mapping.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListingSpecification {
    public static Specification<ListingVehicle> withFilters(FilterDto filterDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();


            // Price range
            if (filterDTO.getStartPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"), filterDTO.getStartPrice()));
            }
            if (filterDTO.getEndPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"), filterDTO.getEndPrice()));
            }

            // Year range
            if (filterDTO.getStartYear() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("startYear"), filterDTO.getStartYear()));
            }
            if (filterDTO.getEndYear() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("endYear"), filterDTO.getEndYear()));
            }

            if (filterDTO.getBody() != null) {
                predicates.add(criteriaBuilder.equal(root.get("body").get("id"), filterDTO.getBody()));
            }

            if (filterDTO.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("body").get("type").get("id"), filterDTO.getType()));
            }

            // Make and Model
            if (filterDTO.getMake() != null) {
                predicates.add(criteriaBuilder.equal(root.get("model").get("make").get("id"), filterDTO.getMake()));
            }
            if (filterDTO.getModel() != null) {
                predicates.add(criteriaBuilder.equal(root.get("model").get("id"), filterDTO.getModel()));
            }

            if(filterDTO.getUserId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("userEntity").get("id"), filterDTO.getUserId()));
            }

            // Engine
            if (filterDTO.getEngine() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("engine").get("id"), filterDTO.getEngine()));
            }

            // Gearbox
            if (filterDTO.getGearbox() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("gearbox").get("id"), filterDTO.getGearbox()));
            }

            // Location
            if (filterDTO.getLocation() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("location").get("id"), filterDTO.getLocation()));
            }

            // Region (assuming region is part of location)
            if (filterDTO.getRegion() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("location").get("region").get("id"), filterDTO.getRegion()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
