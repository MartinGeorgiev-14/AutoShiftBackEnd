package com.cars.carSaleWebsite.models.entities.vehicle;

import com.cars.carSaleWebsite.models.abstracts.BaseAbstract;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
public class Type extends BaseAbstract {
    public String type;
}
