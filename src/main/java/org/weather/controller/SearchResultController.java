package org.weather.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.weather.dto.LocationNameDto;
import org.weather.dto.LocationDto;
import org.weather.dto.LocationToSaveDto;
import org.weather.dto.SessionIdDto;
import org.weather.service.SessionService;
import org.weather.service.WeatherService;
import org.weather.validator.CookieParamValidatorAndHandler;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/search-results")
public class SearchResultController {
    private final SessionService sessionService;
    private final WeatherService weatherService;
    private final CookieParamValidatorAndHandler validatorAndHandler;


    @Autowired
    public SearchResultController(SessionService sessionService, WeatherService weatherService, CookieParamValidatorAndHandler validatorAndHandler) {
        this.sessionService = sessionService;
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

        SessionIdDto currentSessionIdDto = validatorAndHandler.getCurrentSession(sessionIdParam);
        String sessionIdStr = String.valueOf(currentSessionIdDto.getSessionId());

        LocationNameDto locationNameDto = new LocationNameDto(locationNameParam);
        List<LocationDto> locations = weatherService.getLocationByCityName(locationNameDto);

        model.addAttribute("locations", locations);

        ResponseCookie sessionId = ResponseCookie.from("sessionId", sessionIdStr)
                .httpOnly(true)
                .path("/")
                .maxAge(60 * 1)
                .build();

        response.addHeader("Set-Cookie", sessionId.toString());
        return "search-results";
    }
}
