package com.nvfredy.testweatherapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseOpenWeatherMapDto {

    private String name;
    private Main main;

    @Getter
    @Setter
    public static class Main {
        private double temp;
    }
}
