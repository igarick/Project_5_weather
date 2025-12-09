package org.weather.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        weatherByCoordinates.setCity(locationSavedDto.getCity());
        weatherByCoordinates.setLatitude(locationSavedDto.getLatitude());
        weatherByCoordinates.setLongitude(locationSavedDto.getLongitude());
        log.info("Weather by coordinates city = {}, lat = {}, lon = {} mapped", weatherByCoordinates.getCity(), weatherByCoordinates.getLatitude(), weatherByCoordinates.getLongitude());

        return weatherByCoordinates;
    }
}
