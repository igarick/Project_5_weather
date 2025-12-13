package org.weather.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.weather.dto.location.LocationDto;
import org.weather.dto.location.LocationNameDto;
import org.weather.service.SessionService;
import org.weather.service.WeatherService;
import org.weather.validator.CookieParamValidatorAndHandler;

import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/search-results")
public class SearchResultController {
    private final WeatherService weatherService;
    private final CookieParamValidatorAndHandler validatorAndHandler;
    private final SessionService sessionService;


    @Autowired
    public SearchResultController(WeatherService weatherService, CookieParamValidatorAndHandler validatorAndHandler, SessionService sessionService) {
        this.weatherService = weatherService;
        this.validatorAndHandler = validatorAndHandler;
        this.sessionService = sessionService;
    }

    @GetMapping
    public String getLocations() {
        return "search-results";
    }

    @PostMapping
    public String searchLocation(@RequestParam("locationName") String locationNameParam,
                                 @CookieValue(value = "sessionId", defaultValue = "") String sessionIdParam,
                                 Model model) {
        if (!StringUtils.hasText(locationNameParam)) {
            return "search-results";
        }

        UUID sessionId = validatorAndHandler.extractSessionId(sessionIdParam);
        validatorAndHandler.validateSessionExists(sessionId);
        String username = sessionService.getLoginById(sessionId);

        LocationNameDto locationNameDto = new LocationNameDto(locationNameParam);
        List<LocationDto> locations = weatherService.getLocationByCityName(locationNameDto);

        model.addAttribute("locations", locations);
        model.addAttribute("username", username);

        return "search-results";
    }
}
