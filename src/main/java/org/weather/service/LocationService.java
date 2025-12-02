package org.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.weather.dto.LocationToSaveDto;
import org.weather.model.Location;
import org.weather.model.Session;
import org.weather.model.User;
import org.weather.repository.LocationRepository;
import org.weather.repository.SessionRepository;

import java.util.Optional;

@Service
public class LocationService {
    private final Logger log = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;
    private final SessionService sessionService;
    private final SessionRepository sessionRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository, SessionService sessionService, SessionRepository sessionRepository) {
        this.locationRepository = locationRepository;
        this.sessionService = sessionService;
        this.sessionRepository = sessionRepository;
    }

    public void save(LocationToSaveDto locationDto) {
        Optional<Session> sessionOptional = sessionRepository.findById(locationDto.getSessionId());
        Session session = sessionOptional.get();
        Long id = session.getUser().getId();

        Location location = Location.builder()
                .name(locationDto.getLocationName())
                .user(User.builder()
                        .id(id)
                        .build())
                .latitude(locationDto.getLatitude())
                .longitude(locationDto.getLongitude())
                .build();
        locationRepository.save(location);
        log.info("Location saved");
    }
}
