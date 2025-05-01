package com.cars.carSaleWebsite.service;

import com.cars.carSaleWebsite.models.entities.email.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);

    void sendDailyFavoriteFilterNotification() throws Exception;
}
