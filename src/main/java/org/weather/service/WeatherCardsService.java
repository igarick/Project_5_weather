package org.weather.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weather.dto.location.LocationSavedDto;
import org.weather.dto.session.SessionIdDto;
import org.weather.dto.weather.WeatherDto;
import org.weather.dto.weather.WeatherViewDto;
import org.weather.utils.WeatherMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WeatherCardsService {
    private final LocationService locationService;
    private final WeatherService weatherService;
    private final WeatherMapper weatherMapper;

    public List<WeatherViewDto> getWeatherCards(SessionIdDto sessionIdDto) {
        List<LocationSavedDto> locations = locationService.findAllBySessionId(sessionIdDto);

        return locations.stream()
                .map(this::getWeather)
                .map(weatherMapper::toView)
                .toList();
    }

    private WeatherDto getWeather(LocationSavedDto locationSavedDto) {
        WeatherDto weatherByCoordinates = weatherService.getWeatherByCoordinates(
                locationSavedDto.getLatitude(),
                locationSavedDto.getLongitude()
        );
        WeatherDto mapped = WeatherDto.builder()
                .weatherInfo(weatherByCoordinates.getWeatherInfo())
                .weatherBasePrams(weatherByCoordinates.getWeatherBasePrams())
                .country(weatherByCoordinates.getCountry())
                .city(locationSavedDto.getCity())
                .latitude(locationSavedDto.getLatitude())
                .longitude(locationSavedDto.getLongitude())
                .build();
        log.info("Weather by coordinates city = {}, lat = {}, lon = {} mapped", mapped.getCity(), mapped.getLatitude(), mapped.getLongitude());

        return mapped;
    }
}
