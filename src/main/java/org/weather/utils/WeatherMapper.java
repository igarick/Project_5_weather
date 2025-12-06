package org.weather.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.weather.dto.weather.WeatherDto;
import org.weather.dto.weather.WeatherInfoDto;
import org.weather.dto.weather.WeatherViewDto;
import org.weather.exception.ErrorInfo;
import org.weather.exception.MappingException;

@Slf4j
@Component
public class WeatherMapper {
    public WeatherViewDto toView(WeatherDto weatherDto) {
        log.info("Incoming WeatherDto: {}", weatherDto);
        try {
            String description = weatherDto.getWeatherInfo().getFirst().getDescription();
            String capitalized = description.substring(0, 1).toUpperCase() + description.substring(1);

            return WeatherViewDto.builder()
                    .country(weatherDto.getCountry().getCountry())
                    .city(weatherDto.getCity())
                    .temperature(round(weatherDto.getWeatherBasePrams().getTemperature()))
                    .feelsLike(round(weatherDto.getWeatherBasePrams().getFeelsLike()))
                    .humidity(weatherDto.getWeatherBasePrams().getHumidity())
                    .description(capitalized)
                    .icon(weatherDto.getWeatherInfo().getFirst().getIcon())
                    .build();
        } catch (Exception e) {
            throw new MappingException(ErrorInfo.MAPPING_RESPONSE_API_ERROR, e);
        }

    }

    private int round(double value) {
        return (int) Math.round(value);
    }
}
