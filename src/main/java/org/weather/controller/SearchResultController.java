package org.weather.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.weather.dto.LocationDto;
import org.weather.dto.LocationNameDto;
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


    @Autowired
    public SearchResultController(WeatherService weatherService, CookieParamValidatorAndHandler validatorAndHandler) {
        this.weatherService = weatherService;
        this.validatorAndHandler = validatorAndHandler;
    }

    @GetMapping
    public String getLocations() {
        return "search-results";
    }

    @PostMapping
    public String searchLocation(@RequestParam("locationName") String locationNameParam,     //@ModelAttribute("localName") String localNameParam,
                                 @CookieValue(value = "sessionId", defaultValue = "") String sessionIdParam,
                                 Model model, HttpServletResponse response) {
        if(!StringUtils.hasText(locationNameParam)) {
            log.info("Input is empty");
            return "search-results";
        }

        UUID sessionId = validatorAndHandler.extractSessionId(sessionIdParam);
        validatorAndHandler.validateSessionExists(sessionId);

        LocationNameDto locationNameDto = new LocationNameDto(locationNameParam);
        List<LocationDto> locations = weatherService.getLocationByCityName(locationNameDto);

        model.addAttribute("locations", locations);
        return "search-results";
    }
}
