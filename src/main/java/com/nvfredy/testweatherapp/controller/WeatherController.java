package com.nvfredy.testweatherapp.controller;

import com.nvfredy.testweatherapp.domain.Weather;
import com.nvfredy.testweatherapp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/get/{city}/{date}")
    public ResponseEntity<List<Weather>> getWeather(@PathVariable String city,
                                                    @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {

        return ResponseEntity.ok(weatherService.getWeather(city, date));

    }
}
