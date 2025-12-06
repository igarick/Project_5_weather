package org.weather.dto.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDto {
    @JsonProperty("weather")
    private List<WeatherInfoDto> weatherInfo;
    @JsonProperty("main")
    private WeatherBasePramsDto weatherBasePrams;
    @JsonProperty("name")
    private String city;
    @JsonProperty("sys")
    private CountryDto country;

    private BigDecimal latitude;
    private BigDecimal longitude;
}
