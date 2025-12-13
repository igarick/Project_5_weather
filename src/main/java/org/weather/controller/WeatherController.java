package org.weather.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.weather.dto.session.SessionIdDto;
import org.weather.dto.weather.WeatherViewDto;
import org.weather.service.SessionService;
import org.weather.service.WeatherCardsService;
import org.weather.validator.CookieParamValidatorAndHandler;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class WeatherController {
    private final CookieParamValidatorAndHandler validatorAndHandler;
    private final WeatherCardsService weatherCardsService;
    private final SessionService sessionService;

    @GetMapping
    public String home(@CookieValue(value = "sessionId", defaultValue = "") String sessionIdParam,
                       Model model) {
        UUID sessionId = validatorAndHandler.extractSessionId(sessionIdParam);
        SessionIdDto sessionIdDto = new SessionIdDto(sessionId);
        SessionIdDto currentSessionIdDto = validatorAndHandler.getCurrentSession(sessionIdDto);

        List<WeatherViewDto> weatherCards = weatherCardsService.getWeatherCards(currentSessionIdDto);
        String username = sessionService.getLoginById(sessionId);

        model.addAttribute("cards", weatherCards);
        model.addAttribute("username", username);
        return "index";
    }
}
