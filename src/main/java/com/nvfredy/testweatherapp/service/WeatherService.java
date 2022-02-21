package com.nvfredy.testweatherapp.service;

import com.nvfredy.testweatherapp.dto.ResponseOpenWeatherMapDto;
import com.nvfredy.testweatherapp.dto.ResponseWeatherApiDto;
import com.nvfredy.testweatherapp.dto.ResponseWeatherBitDto;
import com.nvfredy.testweatherapp.repository.WeatherRepository;
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
    private final WeatherRepository weatherRepository;

    @Value("${private.open-weather-map.key}")
    private String openWeatherMapPrivateKey;
    @Value("${private.weather-api.key}")
    private String weatherApiPrivateKey;
    @Value("${private.weather-bit.key}")
    private String weatherBitPrivateKey;

    public void getCurrentWeatherByCityFromOpenWeatherMap(String city) {
        ResponseOpenWeatherMapDto weather = restTemplate.getForObject(
                String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", city, openWeatherMapPrivateKey),
                ResponseOpenWeatherMapDto.class);

        if (weather != null) {

            double tempCel = weather.getMain().getTemp() - 273.15;

            weather.getMain().setTemp(tempCel);

        }

        log.info(weather.getName() + " " + weather.getMain().getTemp());

    }

        public void getCurrentWeatherByCityFromWeatherApi(String city) {
            ResponseWeatherApiDto weather = restTemplate.getForObject(
                String.format("https://api.weatherapi.com/v1/current.json?key=%s&q=%s&aqi=no", weatherApiPrivateKey, city),
                    ResponseWeatherApiDto.class);


            log.info(weather.getLocation().getName() + " " + weather.getCurrent().getTemp());
    }

    public void getCurrentWeatherByCityFromWeatherBit(String city) {
        ResponseWeatherBitDto weather = restTemplate.getForObject(
                String.format("https://api.weatherbit.io/v2.0/current?key=%s&city=%s", weatherBitPrivateKey, city),
                ResponseWeatherBitDto.class);

        log.info(weather.getData()[0].getCity() + " " + weather.getData()[0].getTemp());
    }

}
