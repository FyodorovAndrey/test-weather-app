package com.nvfredy.testweatherapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseWeatherApiDto {

    private Location location;
    private Current current;

    @Getter
    @Setter
    public static class Location {
        private String name;
    }

    @Getter
    @Setter
    public static class Current {
        @JsonProperty("temp_c")
        private String temp;
    }
}
