package com.cars.carSaleWebsite.models.entities;

import com.cars.carSaleWebsite.models.abstracts.BaseAbstract;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "roles")
public class Role extends BaseAbstract {

    private String name;
}
