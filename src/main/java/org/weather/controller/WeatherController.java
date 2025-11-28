package org.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.weather.dto.LocationDto;
import org.weather.service.WeatherService;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public void getWeather() throws IOException, InterruptedException {
        String city = "San Francisco";
        List<LocationDto> location = weatherService.getLocationByCityName(city);
        System.out.println(location);


//        String weather = weatherService.getWeatherByCoordinates();
//        System.out.println(weather);
    }
}
