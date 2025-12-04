package org.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.weather.dto.LocationSavedDto;
import org.weather.dto.SessionIdDto;
import org.weather.dto.WeatherDto;
import org.weather.service.LocationService;
import org.weather.service.WeatherCardsService;
import org.weather.service.WeatherService;
import org.weather.validator.CookieParamValidatorAndHandler;

import java.util.List;

@Controller
@RequestMapping("/")
public class WeatherController {
    private final CookieParamValidatorAndHandler validatorAndHandler;
    private final LocationService locationService;
    private final WeatherService weatherService;
    private final WeatherCardsService weatherCardsService;

    @Autowired
    public WeatherController(CookieParamValidatorAndHandler validatorAndHandler,
                             LocationService locationService,
                             WeatherService weatherService, WeatherCardsService weatherCardsService) {
        this.validatorAndHandler = validatorAndHandler;
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.weatherCardsService = weatherCardsService;
    }

    @GetMapping
    public String home(@CookieValue(value = "sessionId", defaultValue = "") String sessionIdParam,
                       Model model) {

        SessionIdDto currentSessionIdDto = validatorAndHandler.getCurrentSession(sessionIdParam);

        List<WeatherDto> weatherCards = weatherCardsService.getWeatherCards(currentSessionIdDto);

        model.addAttribute("cards", weatherCards);

        return "index";
    }
}
