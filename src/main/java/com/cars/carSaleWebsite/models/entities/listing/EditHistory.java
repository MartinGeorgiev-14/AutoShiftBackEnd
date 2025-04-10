package com.cars.carSaleWebsite.models.entities.listing;

import com.cars.carSaleWebsite.models.abstracts.BaseAbstract;
import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class EditHistory extends BaseAbstract {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_editor_id")
    private UserEntity editor;
    private Date editDate;
}
