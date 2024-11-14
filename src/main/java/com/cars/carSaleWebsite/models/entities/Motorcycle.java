package com.cars.carSaleWebsite.models.entities;

import com.cars.carSaleWebsite.models.abstracts.VehicleAbstract;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Motorcycle extends VehicleAbstract {
    private int engineDisplacement;
    //test
}
