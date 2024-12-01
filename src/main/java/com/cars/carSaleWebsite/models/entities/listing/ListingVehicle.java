package com.cars.carSaleWebsite.models.entities.listing;

import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.vehicle.*;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;

    private BigDecimal price;
    private Date createdAt;
    private Date editedAt;
    private int horsepower;
    private int mileage;

    private String description;
    @Column(nullable = false)
    private Integer engineDisplacement;

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
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

}
