package org.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weather.dto.LocationSavedDto;
import org.weather.dto.SessionIdDto;
import org.weather.dto.weather.WeatherDto;
import org.weather.dto.weather.WeatherViewDto;
import org.weather.utils.WeatherMapper;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WeatherCardsService {
    private final LocationService locationService;
    private final WeatherService weatherService;
    private final WeatherMapper weatherMapper;

    @Autowired
    public WeatherCardsService(LocationService locationService, WeatherService weatherService, WeatherMapper weatherMapper) {
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.weatherMapper = weatherMapper;
    }

    public List<WeatherViewDto> getWeatherCards(SessionIdDto sessionIdDto) {
        List<LocationSavedDto> locations = locationService.findAllBySession(sessionIdDto);

        return locations.stream()
                .map(this::getWeather)
                .map(weatherMapper::toView)
                .toList();
    }

    private WeatherDto getWeather(LocationSavedDto locationSavedDto) {
        WeatherDto weatherByCoordinates = weatherService.getWeatherByCoordinates(
                locationSavedDto.getLatitude().toString(),
                locationSavedDto.getLongitude().toString()
        );
        weatherByCoordinates.setCity(locationSavedDto.getCity());
        return weatherByCoordinates;
    }

//    private WeatherViewDto mapTo(WeatherDto weatherDto) {
//        String description = weatherDto.getWeatherInfo().getFirst().getDescription();
//        String capitalized = description.substring(0, 1).toUpperCase() + description.substring(1);
//
//        return WeatherViewDto.builder()
//                .country(weatherDto.getCountry().getCountry())
//                .city(weatherDto.getCity())
//                .temperature((int) Math.round(weatherDto.getWeatherBasePrams().getTemperature()))
//                .feelsLike((int) Math.round(weatherDto.getWeatherBasePrams().getFeelsLike()))
//                .humidity(weatherDto.getWeatherBasePrams().getHumidity())
//                .description(capitalized)
//                .icon(weatherDto.getWeatherInfo().getFirst().getIcon())
//                .build();
//    }
}
