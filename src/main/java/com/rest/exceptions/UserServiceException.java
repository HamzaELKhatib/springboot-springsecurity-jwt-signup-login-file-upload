package com.rest.exceptions;


public class UserServiceException extends RuntimeException {

    private static final long serialVersionUID = 6401866352848257408L;

    public UserServiceException(String message) {
        super(message);
    }
}

