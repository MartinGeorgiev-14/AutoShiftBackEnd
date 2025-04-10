package com.cars.carSaleWebsite.helpers;

import com.cars.carSaleWebsite.models.nonEntity.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageCreator {

    public Message create(Boolean isDisplayed, String title, String description, String type){
        return new Message(isDisplayed, title, description, type);
    }
}
