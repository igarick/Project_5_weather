package org.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weather.dto.LocationSavedDto;
import org.weather.dto.SessionIdDto;
import org.weather.dto.WeatherDto;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WeatherCardsService {
    private final LocationService locationService;
    private final WeatherService weatherService;

    @Autowired
    public WeatherCardsService(LocationService locationService, WeatherService weatherService) {
        this.locationService = locationService;
        this.weatherService = weatherService;
    }

    public List<WeatherDto> getWeatherCards(SessionIdDto sessionIdDto) {
        List<LocationSavedDto> locations = locationService.findAllBySession(sessionIdDto);

        return locations.stream()
                .map(this::buildWeatherDto)
                .toList();
    }

    private WeatherDto buildWeatherDto(LocationSavedDto locationSavedDto) {
        WeatherDto weatherByCoordinates = weatherService.getWeatherByCoordinates(
                locationSavedDto.getLatitude().toString(),
                locationSavedDto.getLongitude().toString()
        );

        weatherByCoordinates.setCity(locationSavedDto.getLocationName());

        return weatherByCoordinates;
    }
}
