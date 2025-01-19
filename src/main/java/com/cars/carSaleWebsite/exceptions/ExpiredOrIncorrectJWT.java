package com.cars.carSaleWebsite.exceptions;

public class ExpiredOrIncorrectJWT extends RuntimeException {
    private static final long serialVersionUID = 4;

    public ExpiredOrIncorrectJWT(String message) {
        super(message);
    }
}
