package org.weather.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.weather.dto.LocationNameDto;
import org.weather.dto.LocationDto;
import org.weather.dto.SessionIdDto;
import org.weather.service.SessionService;
import org.weather.service.WeatherService;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/search-results")
public class SearchResultController {
    private final SessionService sessionService;
    private final WeatherService weatherService;

    @Autowired
    public SearchResultController(SessionService sessionService, WeatherService weatherService) {
        this.sessionService = sessionService;
        this.weatherService = weatherService;
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
            return "search-results";
        }

        if (!StringUtils.hasText(sessionIdParam)) {
            return "redirect:/auth/sign-in";
        }

        UUID sessionIdFomCookies = null;
        try {
            sessionIdFomCookies = UUID.fromString(sessionIdParam);
        } catch (IllegalArgumentException e) {
            return "redirect:/auth/sign-in";
        }

        SessionIdDto sessionIdFomCookiesDto = new SessionIdDto(sessionIdFomCookies);
        Optional<SessionIdDto> currentSession = sessionService.findCurrentSession(sessionIdFomCookiesDto);
        if (currentSession.isEmpty()) {
            return "redirect:/auth/sign-in";
        }
        String currentSessionId = String.valueOf(currentSession.get().getSessionId());

        LocationNameDto locationNameDto = new LocationNameDto(locationNameParam);
        List<LocationDto> locations = weatherService.getLocationByCityName(locationNameDto);

        model.addAttribute("locations", locations);

        ResponseCookie sessionId = ResponseCookie.from("sessionId", currentSessionId)
                .httpOnly(true)
                .path("/")
                .maxAge(60 * 10)
                .build();

        response.addHeader("Set-Cookie", sessionId.toString());
        return "search-results";
    }
}
