package com.cars.carSaleWebsite.exceptions;

public class EngineNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 2;

    public EngineNotFoundException(String message) {
        super(message);
    }
}
