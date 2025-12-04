package org.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.weather.dto.LocationSavedDto;
import org.weather.dto.SessionIdDto;
import org.weather.dto.WeatherDto;
import org.weather.service.LocationService;
import org.weather.service.WeatherService;
import org.weather.validator.CookieParamValidatorAndHandler;

import java.util.List;

@Controller
@RequestMapping("/")
public class WeatherController {
    private final CookieParamValidatorAndHandler validatorAndHandler;
    private final LocationService locationService;
    private final WeatherService weatherService;

    @Autowired
    public WeatherController(CookieParamValidatorAndHandler validatorAndHandler, LocationService locationService, WeatherService weatherService) {
        this.validatorAndHandler = validatorAndHandler;
        this.locationService = locationService;
        this.weatherService = weatherService;
    }

    @GetMapping
    public String home(@CookieValue(value = "sessionId", defaultValue = "") String sessionIdParam) {

        SessionIdDto currentSessionIdDto = validatorAndHandler.getCurrentSession(sessionIdParam);
        List<LocationSavedDto> locationSavedDtos = locationService.findAllBySession(currentSessionIdDto);

        WeatherDto weatherByCoordinates = weatherService.getWeatherByCoordinates("465", "465");



        return "index";
    }
//    private final WeatherService weatherService;
//
//    @Autowired
//    public WeatherController(WeatherService weatherService) {
//        this.weatherService = weatherService;
//    }
//
//    @GetMapping
//    public void getWeather() throws IOException, InterruptedException {
//        String city = "San Francisco";
//        LocationNameDto locationNameDto = new LocationNameDto(city);
//        List<LocationDto> location = weatherService.getLocationByCityName(locationNameDto);
//        System.out.println(location);


//        String weather = weatherService.getWeatherByCoordinates();
//        System.out.println(weather);
//    }
}
