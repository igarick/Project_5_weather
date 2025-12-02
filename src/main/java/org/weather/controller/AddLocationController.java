package org.weather.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.weather.dto.LocationToSaveDto;
import org.weather.service.LocationService;
import org.weather.validator.CookieParamValidatorAndHandler;

import java.math.BigDecimal;
import java.util.UUID;

@Controller
@RequestMapping("/location/add")
public class AddLocationController {
    private final Logger log = LoggerFactory.getLogger(AddLocationController.class);

    private final CookieParamValidatorAndHandler validatorAndHandler;
    private final LocationService locationService;

    @Autowired
    public AddLocationController(CookieParamValidatorAndHandler validatorAndHandler, LocationService locationService) {
        this.validatorAndHandler = validatorAndHandler;
        this.locationService = locationService;
    }

    @PostMapping
    public String addLocation(@CookieValue(value = "sessionId", defaultValue = "") String sessionIdParam,
                              @RequestParam("locationName") String locationNameParam,
                              @RequestParam("latitude") String latitudeParam,
                              @RequestParam("longitude") String longitudeParam
                              ) {

        if ((locationNameParam == null || locationNameParam.isBlank())
            || (latitudeParam == null || latitudeParam.isBlank())
            || (longitudeParam == null || longitudeParam.isBlank())) {
            log.info("Validation location params failed");
            return "redirect:/search-results";
        }

        UUID sessionId = validatorAndHandler.extractSessionId(sessionIdParam);

        LocationToSaveDto location = LocationToSaveDto.builder()
                .sessionId(sessionId)
                .locationName(locationNameParam)
                .latitude(new BigDecimal(latitudeParam))
                .longitude(new BigDecimal(longitudeParam))
                .build();

        locationService.save(location);

        return null;
    }
}
