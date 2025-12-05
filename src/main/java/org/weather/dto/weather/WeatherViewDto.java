package org.weather.dto.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class WeatherViewDto {
    private String country;
    private String city;
    private int temperature;
    private int feelsLike;
    private int humidity;
    private String description;
    private String icon;
}
