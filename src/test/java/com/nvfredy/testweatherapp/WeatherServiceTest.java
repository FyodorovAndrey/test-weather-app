package com.nvfredy.testweatherapp;

import com.nvfredy.testweatherapp.exception.WeatherNotFoundException;
import com.nvfredy.testweatherapp.repository.WeatherRepository;
import com.nvfredy.testweatherapp.service.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;
    @Autowired
    private WeatherRepository weatherRepository;

    @Test
    void getWeatherFromOmskAndWeatherShouldBeSaved() {
        long countRecordsInDB = weatherRepository.count();

        weatherService.getWeather("Omsk", LocalDate.now());

        assertEquals(countRecordsInDB + 1, weatherRepository.count());
    }

    @Test
    public void expectedWeatherNotFoundException() {
        assertThrows(WeatherNotFoundException.class, () -> {
            weatherService.getWeather("sdfjgfdshgjfd", LocalDate.now());
        });
    }
}