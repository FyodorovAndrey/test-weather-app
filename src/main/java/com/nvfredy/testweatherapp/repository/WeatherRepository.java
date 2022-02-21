package com.nvfredy.testweatherapp.repository;

import com.nvfredy.testweatherapp.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {

    @Query("select w from Weather w where lower(w.cityName) like lower(concat('%', :cityName,'%')) and w.date = :date")
    Weather findByCityAndDate(@Param("cityName") String name, @Param("date") LocalDate date);
}
