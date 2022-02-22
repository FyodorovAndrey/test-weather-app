package com.nvfredy.testweatherapp.service;

import com.nvfredy.testweatherapp.domain.Weather;
import com.nvfredy.testweatherapp.dto.ResponseOpenWeatherMapDto;
import com.nvfredy.testweatherapp.dto.ResponseWeatherApiDto;
import com.nvfredy.testweatherapp.dto.ResponseWeatherBitDto;
import com.nvfredy.testweatherapp.exception.WeatherNotFoundException;
import com.nvfredy.testweatherapp.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@EnableScheduling
public class WeatherService {

    private final RestTemplate restTemplate;
    private final WeatherRepository weatherRepository;

    @Value("${private.open-weather-map.key}")
    private String openWeatherMapPrivateKey;
    @Value("${private.weather-api.key}")
    private String weatherApiPrivateKey;
    @Value("${private.weather-bit.key}")
    private String weatherBitPrivateKey;
    @Value("${private.cities}")
    private String cities;

    @Scheduled(cron = "${private.schedule}")
    public void saveWeatherScheduled() {
        String[] citiesArray = cities.split(" ");

        for (String city : citiesArray) {
            saveWeather(city);
        }

    }

    public List<Weather> getWeather(String city, LocalDate date) {
        List<Weather> weathers = getWeatherByCityAndDate(city, date);

        if (weathers.size() == 0) {
            return Collections.singletonList(saveWeather(city));
        }

        return weathers;
    }

    private double getCurrentWeatherByCityFromOpenWeatherMap(String city) {

        ResponseOpenWeatherMapDto weather;

        try {
            weather = restTemplate.getForObject(
                    String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", city, openWeatherMapPrivateKey),
                    ResponseOpenWeatherMapDto.class);
        } catch (HttpClientErrorException e) {
            throw new WeatherNotFoundException(HttpStatus.BAD_REQUEST, "OpenWeatherMap request failed!");
        }


        if (weather != null) {

            double tempCel = weather.getMain().getTemp() - 273.15;

            weather.getMain().setTemp(tempCel);

            log.info(String.format("Data from OpenWeatherMap: %s - %,.2f", weather.getName(), weather.getMain().getTemp()));

        }

        return weather.getMain().getTemp();
    }

    private double getCurrentWeatherByCityFromWeatherApi(String city) {

        ResponseWeatherApiDto weather;

        try {
            weather = restTemplate.getForObject(
                    String.format("https://api.weatherapi.com/v1/current.json?key=%s&q=%s&aqi=no", weatherApiPrivateKey, city),
                    ResponseWeatherApiDto.class);
        } catch (HttpClientErrorException e) {
            throw new WeatherNotFoundException(HttpStatus.BAD_REQUEST, "WeatherApi request failed!");
        }

        if (weather != null) {
            log.info(String.format("Data from WeatherApi: %s - %,.2f", weather.getLocation().getName(), weather.getCurrent().getTemp()));
        }

        return weather.getCurrent().getTemp();
    }

    private double getCurrentWeatherByCityFromWeatherBit(String city) {

        ResponseWeatherBitDto weather;

        try {
            weather = restTemplate.getForObject(
                    String.format("https://api.weatherbit.io/v2.0/current?key=%s&city=%s", weatherBitPrivateKey, city),
                    ResponseWeatherBitDto.class);
        } catch (HttpClientErrorException e) {
            throw new WeatherNotFoundException(HttpStatus.BAD_REQUEST, "WeatherBit request failed!");
        }

        if (weather != null) {
            log.info(String.format("Data from WeatherBit: %s - %,.2f", weather.getData()[0].getCity(), weather.getData()[0].getTemp()));
        }

        return weather.getData()[0].getTemp();
    }

    private double findAverageTemperatureFromAllServices(String city) {

        double[] temperatures = new double[3];

        temperatures[0] = getCurrentWeatherByCityFromOpenWeatherMap(city);
        temperatures[1] = getCurrentWeatherByCityFromWeatherApi(city);
        temperatures[2] = getCurrentWeatherByCityFromWeatherBit(city);

        return Arrays.stream(temperatures).average().getAsDouble();
    }

    private Weather saveWeather(String city) {

        Weather weather = new Weather();
        weather.setCityName(city);
        BigDecimal bd = BigDecimal.valueOf(findAverageTemperatureFromAllServices(city)).setScale(2, RoundingMode.HALF_UP);
        weather.setTemperature(bd.doubleValue());
        weather.setDateTime(LocalDateTime.now());

        weatherRepository.save(weather);

        log.info("Weather saved successfully {}", weather);

        return weather;
    }


    private List<Weather> getWeatherByCityAndDate(String city, LocalDate date) {
        return weatherRepository.findByCityNameContainingIgnoreCaseAndDateTimeBetween(city, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }
}
