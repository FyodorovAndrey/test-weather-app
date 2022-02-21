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

    @GetMapping("/{city}")
    public void getCurrentWeather(@PathVariable String city) {
        weatherService.getCurrentWeatherByCityFromOpenWeatherMap(city);
    }
}
