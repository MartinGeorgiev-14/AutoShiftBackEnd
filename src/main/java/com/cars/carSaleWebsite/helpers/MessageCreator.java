package com.cars.carSaleWebsite.helpers;

import com.cars.carSaleWebsite.models.nonEntity.Message;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageCreator {

    private final BodyCreator bodyCreator;

    public MessageCreator(BodyCreator bodyCreator) {
        this.bodyCreator = bodyCreator;
    }

    public Message create(Boolean isDisplayed, String title, String description, String type){
        return new Message(isDisplayed, title, description, type);
    }

    public Message reassignProps(Message message, Boolean isDisplayed, String title, String description, String type){
            message.setIsDisplayed(isDisplayed);
            message.setTitle(title);
            message.setDescription(description);
            message.setType(type);

            return message;
    }

    public Map<String, Object> createErrorResponse(String reason, HttpStatusCode status){
        Map<String, Object> body = bodyCreator.create();

        Message message = create(true, "Error", reason, "error");
        body.put("message", message);
        body.put("status", status.value());

        return body;
    }
}
