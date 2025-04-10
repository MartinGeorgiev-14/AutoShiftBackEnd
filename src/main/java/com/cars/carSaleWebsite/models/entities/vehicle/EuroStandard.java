package com.cars.carSaleWebsite.models.entities.vehicle;

import com.cars.carSaleWebsite.models.abstracts.BaseAbstract;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class EuroStandard extends BaseAbstract {
    private String standard;
}
