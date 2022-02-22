package com.nvfredy.testweatherapp.repository;

import com.nvfredy.testweatherapp.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    List<Weather> findByCityNameContainingIgnoreCaseAndDateTimeBetween(String name, LocalDateTime dateFrom, LocalDateTime dateTo);
}
