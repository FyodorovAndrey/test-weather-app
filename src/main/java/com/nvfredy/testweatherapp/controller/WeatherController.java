package com.nvfredy.testweatherapp.controller;

import com.nvfredy.testweatherapp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/open-weather-map/{city}")
    public void getCurrentWeatherFromOpenWeatherMap(@PathVariable String city) {
        weatherService.getCurrentWeatherByCityFromOpenWeatherMap(city);
    }

    @GetMapping("/weather-api/{city}")
    public void getCurrentWeatherFromWeatherApi(@PathVariable String city) {
        weatherService.getCurrentWeatherByCityFromWeatherApi(city);
    }
}
