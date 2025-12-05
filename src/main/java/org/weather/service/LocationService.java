package org.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weather.dto.LocationSavedDto;
import org.weather.dto.LocationToSaveDto;
import org.weather.dto.SessionIdDto;
import org.weather.exception.DaoException;
import org.weather.exception.ErrorInfo;
import org.weather.exception.SessionNotFoundException;
import org.weather.model.Location;
import org.weather.model.Session;
import org.weather.model.User;
import org.weather.repository.LocationRepository;
import org.weather.repository.SessionRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LocationService {
    private final Logger log = LoggerFactory.getLogger(LocationService.class);

    private final LocationRepository locationRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository, SessionRepository sessionRepository) {
        this.locationRepository = locationRepository;
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public void saveLocation(LocationToSaveDto locationDto) {
        log.info("Start saving location {}", locationDto.getLocationName());
        Optional<Session> sessionOptional = sessionRepository.findById(locationDto.getSessionId());
        Session session = sessionOptional.orElseThrow(() -> new SessionNotFoundException(ErrorInfo.SESSION_NOT_FOUND));
        Long userId = session.getUser().getId();

        Location location = Location.builder()
                .name(locationDto.getLocationName())
                .user(User.builder()
                        .id(userId)
                        .build())
                .latitude(locationDto.getLatitude())
                .longitude(locationDto.getLongitude())
                .build();
        try {
            locationRepository.save(location);
        } catch (Exception e) {
            log.error("Failed to save location {}", locationDto);
            throw new DaoException(ErrorInfo.DATA_SAVE_ERROR,e);
        }
        log.info("Location {} saved", locationDto.getLocationName());
    }

    public List<LocationSavedDto> findAllBySession(SessionIdDto sessionIdDto) {
        log.info("Start getting locations by sessionId {}", sessionIdDto);
        Optional<Session> sessionOptional = sessionRepository.findById(sessionIdDto.getSessionId());
        Session session = sessionOptional.orElseThrow(() -> new SessionNotFoundException(ErrorInfo.SESSION_NOT_FOUND));
        Long userId = session.getUser().getId();
        log.info("UserId {} received from session", userId);

        List<Location> locations = locationRepository.findAllByUser_Id(userId);

        List<LocationSavedDto> locationSavedDtos = locations.stream()
                .map(this::buildLocationSavedDto)
                .toList();

        log.info("Received list of locations for userId {}", userId);
        return locationSavedDtos;
    }

    private LocationSavedDto buildLocationSavedDto(Location location) {
        log.info("Build location {}", location.getName());
        return LocationSavedDto.builder()
                .city(location.getName())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }

}
