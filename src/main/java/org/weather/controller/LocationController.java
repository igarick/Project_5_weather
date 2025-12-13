package org.weather.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.weather.dto.location.LocationToDeleteDto;
import org.weather.dto.location.LocationToSaveDto;
import org.weather.service.LocationService;
import org.weather.validator.CookieParamValidatorAndHandler;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {
    private final Logger log = LoggerFactory.getLogger(LocationController.class);

    private final CookieParamValidatorAndHandler validatorAndHandler;
    private final LocationService locationService;

    @PostMapping("/add")
    public String addLocation(@CookieValue(value = "sessionId", defaultValue = "") String sessionIdParam,
                              @RequestParam("locationName") String locationNameParam,
                              @RequestParam("latitude") String latitudeParam,
                              @RequestParam("longitude") String longitudeParam) {
        if (!StringUtils.hasText(locationNameParam) ||
            !StringUtils.hasText(latitudeParam) ||
            !StringUtils.hasText(longitudeParam)) {
            log.error("Coordinates are absent");
            return "redirect:/";
        }
        UUID sessionId = validatorAndHandler.extractSessionId(sessionIdParam);

        LocationToSaveDto location = LocationToSaveDto.builder()
                .sessionId(sessionId)
                .locationName(locationNameParam)
                .latitude(new BigDecimal(latitudeParam))
                .longitude(new BigDecimal(longitudeParam))
                .build();
        locationService.saveLocation(location);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(@CookieValue(value = "sessionId", defaultValue = "") String sessionIdParam,
                         @RequestParam("latitude") String latitudeParam,
                         @RequestParam("longitude") String longitudeParam) {
        if (!StringUtils.hasText(latitudeParam) ||
            !StringUtils.hasText(longitudeParam)) {
            log.error("Coordinates are absent");
            return "redirect:/";
        }
        UUID sessionId = validatorAndHandler.extractSessionId(sessionIdParam);

        LocationToDeleteDto location = LocationToDeleteDto.builder()
                .sessionId(sessionId)
                .latitude(new BigDecimal(latitudeParam))
                .longitude(new BigDecimal(longitudeParam))
                .build();
        locationService.deleteLocation(location);
        return "redirect:/";
    }

}
