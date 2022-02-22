package com.nvfredy.testweatherapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WeatherNotFoundException extends ResponseStatusException {
    public WeatherNotFoundException(HttpStatus httpStatus, String message) {
        super(httpStatus, message);
    }
}
