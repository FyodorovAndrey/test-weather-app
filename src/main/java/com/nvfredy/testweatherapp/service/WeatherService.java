package com.nvfredy.testweatherapp.service;

import com.nvfredy.testweatherapp.domain.Weather;
import com.nvfredy.testweatherapp.dto.ResponseOpenWeatherMapDto;
import com.nvfredy.testweatherapp.dto.ResponseWeatherApiDto;
import com.nvfredy.testweatherapp.dto.ResponseWeatherBitDto;
import com.nvfredy.testweatherapp.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;


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

    public double getCurrentWeatherByCityFromOpenWeatherMap(String city) {
        ResponseOpenWeatherMapDto weather = restTemplate.getForObject(
                String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", city, openWeatherMapPrivateKey),
                ResponseOpenWeatherMapDto.class);

        if (weather != null) {

            double tempCel = weather.getMain().getTemp() - 273.15;

            weather.getMain().setTemp(tempCel);

            log.info(String.format("Data from OpenWeatherMap: %s - %,.2f", weather.getName(), weather.getMain().getTemp()));

        }

        return weather.getMain().getTemp();
    }

    public double getCurrentWeatherByCityFromWeatherApi(String city) {
        ResponseWeatherApiDto weather = restTemplate.getForObject(
                String.format("https://api.weatherapi.com/v1/current.json?key=%s&q=%s&aqi=no", weatherApiPrivateKey, city),
                ResponseWeatherApiDto.class);

        if (weather != null) {
            log.info(String.format("Data from WeatherApi: %s - %,.2f", weather.getLocation().getName(), weather.getCurrent().getTemp()));
        }

        return weather.getCurrent().getTemp();
    }

    public double getCurrentWeatherByCityFromWeatherBit(String city) {
        ResponseWeatherBitDto weather = restTemplate.getForObject(
                String.format("https://api.weatherbit.io/v2.0/current?key=%s&city=%s", weatherBitPrivateKey, city),
                ResponseWeatherBitDto.class);

        if (weather != null) {
            log.info(String.format("Data from WeatherBit: %s - %,.2f", weather.getData()[0].getCity(), weather.getData()[0].getTemp()));
        }

        return weather.getData()[0].getTemp();
    }

    public double findAverageTemperatureFromAllServices(String city) {

        double[] temperatures = new double[3];

        temperatures[0] = getCurrentWeatherByCityFromOpenWeatherMap(city);
        temperatures[1] =getCurrentWeatherByCityFromWeatherApi(city);
        temperatures[2] =getCurrentWeatherByCityFromWeatherBit(city);

        return Arrays.stream(temperatures).average().getAsDouble();
    }

    public void saveWeather(String city) {

        Weather weather = new Weather();
        weather.setCityName(city);
        weather.setTemperature(findAverageTemperatureFromAllServices(city));
        weather.setDate(LocalDate.now());

        weatherRepository.save(weather);

        log.info("Weather saved successfully {}", weather);
    }

    public Weather getWeather(String city, LocalDate date) {
        return weatherRepository.findByCityAndDate(city, date);
    }
}
