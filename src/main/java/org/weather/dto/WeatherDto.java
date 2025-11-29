package org.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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


    //      брать из погоды
//            [
//    {
//             "name":"Innere Stadt",
//            "country":"AT"
//    },
//            [
}
