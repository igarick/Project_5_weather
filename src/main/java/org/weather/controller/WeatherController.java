package org.weather.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.weather.dto.SessionIdDto;
import org.weather.dto.weather.WeatherDto;
import org.weather.dto.weather.WeatherViewDto;
import org.weather.service.WeatherCardsService;
import org.weather.validator.CookieParamValidatorAndHandler;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
public class WeatherController {
    private final CookieParamValidatorAndHandler validatorAndHandler;
    private final WeatherCardsService weatherCardsService;

    @Autowired
    public WeatherController(CookieParamValidatorAndHandler validatorAndHandler,
                             WeatherCardsService weatherCardsService) {
        this.validatorAndHandler = validatorAndHandler;
        this.weatherCardsService = weatherCardsService;
    }

    @GetMapping
    public String home(@CookieValue(value = "sessionId", defaultValue = "") String sessionIdParam,
                       Model model) {
        SessionIdDto currentSessionIdDto = validatorAndHandler.getCurrentSession(sessionIdParam);
        List<WeatherViewDto> weatherCards = weatherCardsService.getWeatherCards(currentSessionIdDto);

        model.addAttribute("cards", weatherCards);
        return "index";
    }
}
