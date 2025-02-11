package com.neha.UserService.exceptions;

public class ValidTokenNotFoundException extends Exception {
    public ValidTokenNotFoundException(String message) {
        super(message);
    }
}
