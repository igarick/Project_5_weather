package org.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weather.dto.LocationToSaveDto;
import org.weather.exception.DaoException;
import org.weather.exception.ErrorInfo;
import org.weather.exception.SessionNotFoundException;
import org.weather.model.Location;
import org.weather.model.Session;
import org.weather.model.User;
import org.weather.repository.LocationRepository;
import org.weather.repository.SessionRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
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

    @Transactional
    public void save(LocationToSaveDto locationDto) {
        log.info("Start saving location");
        Optional<Session> sessionOptional = sessionRepository.findById(locationDto.getSessionId());
        Session session = sessionOptional.orElseThrow(() -> new SessionNotFoundException(ErrorInfo.SESSION_NOT_FOUND));
        Long id = session.getUser().getId();

        Location location = Location.builder()
                .name(locationDto.getLocationName())
                .user(User.builder()
                        .id(id)
                        .build())
                .latitude(locationDto.getLatitude())
                .longitude(locationDto.getLongitude())
                .build();
        try {
            locationRepository.save(location);
        } catch (Exception e) {
            log.error("Failed to save location");
            throw new DaoException(ErrorInfo.DATA_SAVE_ERROR,e);
        }
        log.info("Location saved");
    }
}
