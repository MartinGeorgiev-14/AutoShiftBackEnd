package com.cars.carSaleWebsite.helpers;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BodyCreator {

    public Map<String, Object> create(){
        return new HashMap<>();
    }
}
