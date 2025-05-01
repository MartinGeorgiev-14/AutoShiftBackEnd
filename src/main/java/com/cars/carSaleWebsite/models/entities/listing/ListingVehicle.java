package com.cars.carSaleWebsite.models.entities.listing;

import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.vehicle.*;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
public class ListingVehicle {
    @Id
    @GeneratedValue
    private UUID id;

    private Boolean isActive;
    @Column(name = "price")
    private BigDecimal price;
    private LocalDate manufactureDate;
    private LocalDate createdAt;
    private LocalDate editedAt;
    private Integer horsepower;
    private Integer mileage;

    @Column(length = 5000)
    private String description;
    @Column(nullable = false)
    private Integer engineDisplacement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "engine_id", nullable = false)
    private Engine engine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gearbox_id", nullable = false)
    private Gearbox gearbox;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_id", nullable = false)
    private Body body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "color_id", referencedColumnName = "id", nullable = false)
    private Color color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "euro_standard_id", referencedColumnName = "id", nullable = false)
    private EuroStandard euroStandard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
}
