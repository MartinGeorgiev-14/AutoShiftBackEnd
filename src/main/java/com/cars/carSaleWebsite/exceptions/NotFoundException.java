package com.cars.carSaleWebsite.exceptions;

public class NotFoundException extends RuntimeException {
    private static final long serialVersionUID = 3;

    public NotFoundException(String message) {
        super(message);
    }
}
