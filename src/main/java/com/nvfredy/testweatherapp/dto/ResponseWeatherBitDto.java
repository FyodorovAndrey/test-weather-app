package com.nvfredy.testweatherapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseWeatherBitDto {

    @JsonProperty("data")
    private DataFromJson[] data;

    @Getter
    @Setter
    public static class DataFromJson {
        @JsonProperty("city_name")
        private String city;
        private double temp;
    }
}
