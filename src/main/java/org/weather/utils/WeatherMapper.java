package org.weather.utils;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.weather.dto.weather.WeatherDto;
import org.weather.dto.weather.WeatherInfoDto;
import org.weather.dto.weather.WeatherViewDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WeatherMapper {
    @Mapping(target = "country", source = "country.country")
    @Mapping(target = "temperature", source = "weatherBasePrams.temperature",
            qualifiedByName = "round")
    @Mapping(target = "feelsLike", source = "weatherBasePrams.feelsLike",
            qualifiedByName = "round")
    @Mapping(target = "humidity", source = "weatherBasePrams.humidity")
    @Mapping(target = "description", source = "weatherInfo",
            qualifiedByName = "capitalizeDescription")
    @Mapping(target = "icon", source = "weatherInfo",
            qualifiedByName = "extractIcon")
    WeatherViewDto toView(WeatherDto weatherDto);

    @Named("round")
    static int round(double value) {
        return (int) Math.round(value);
    }

    @Named("capitalizeDescription")
    static String capitalizeDescription(List<WeatherInfoDto> weatherInfo) {
        if (weatherInfo == null || weatherInfo.isEmpty()) {
            return "N/A";
        }
        String description = weatherInfo.get(0).getDescription();
        return description.substring(0, 1).toUpperCase() + description.substring(1);
    }

    @Named("extractIcon")
    static String extractIcon(List<WeatherInfoDto> weatherInfo) {
        if (weatherInfo == null || weatherInfo.isEmpty()) {
            return null;
        }
        return weatherInfo.get(0).getIcon();
    }
}
