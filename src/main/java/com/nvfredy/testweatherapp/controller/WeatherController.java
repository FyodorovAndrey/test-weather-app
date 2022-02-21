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

    @GetMapping("/weather-bit/{city}")
    public void getCurrentWeatherFromWeatherBit(@PathVariable String city) {
        weatherService.getCurrentWeatherByCityFromWeatherBit(city);
    }

    @GetMapping("/save/{city}")
    public void saveWeather(@PathVariable String city) {
        weatherService.saveWeather(city);
    }

    @GetMapping("/get/{city}/{date}")
    public ResponseEntity<Weather> getWeather(@PathVariable String city, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {


        return ResponseEntity.ok(weatherService.getWeather(city, date));

    }
}
