package com.nvfredy.testweatherapp.service;

import com.nvfredy.testweatherapp.dto.ResponseOpenWeatherMapDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestTemplate restTemplate;

    @Value("${private.open-weather-map.key}")
    private String openWeatherMapPrivateKey;

    public void getCurrentWeatherByCityFromOpenWeatherMap(String city) {
        ResponseOpenWeatherMapDto weather = restTemplate.getForObject(
                String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", city, openWeatherMapPrivateKey),
                ResponseOpenWeatherMapDto.class);

        if (weather != null) {

            double tempCel = weather.getMain().getTemp() - 273.15;

            weather.getMain().setTemp(tempCel);

            System.out.println(weather.getName() + " " + weather.getMain().getTemp());
        }

    }

}
