package com.cars.carSaleWebsite.dto.Listing;

import com.cars.carSaleWebsite.models.entities.user.UserEntity;
import com.cars.carSaleWebsite.models.entities.vehicle.*;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class FavoriteFilterDto {

    private UserEntity userEntity;

    private BigDecimal priceStart;
    private BigDecimal priceEnd;
    private Date manufactureDateStart;
    private Date manufactureDateEnd;
    private Integer horsepowerStart;
    private Integer horsepowerEnd;
    private Integer mileageStart;
    private Integer mileageEnd;
    private Integer engineDisplacementStart;
    private Integer engineDisplacementEnd;

    private Model model;
    private Engine engine;
    private Gearbox gearbox;
    private Body body;
    private Color color;
    private EuroStandard euroStandard;
    private Location location;
}
