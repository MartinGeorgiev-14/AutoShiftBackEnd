package com.cars.carSaleWebsite.models.abstracts;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@MappedSuperclass
public abstract class BaseAbstract {
    @Id
    @GeneratedValue
    protected UUID id;
}
