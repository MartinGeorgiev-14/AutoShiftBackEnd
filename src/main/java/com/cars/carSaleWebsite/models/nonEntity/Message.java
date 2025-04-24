package com.cars.carSaleWebsite.models.nonEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Boolean isDisplayed;
    private String title;
    private String description;
    private String type;

}
