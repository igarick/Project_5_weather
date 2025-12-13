package org.weather.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weather.dto.location.LocationSavedDto;
import org.weather.dto.location.LocationToDeleteDto;
import org.weather.dto.location.LocationToSaveDto;
import org.weather.dto.session.SessionIdDto;
import org.weather.exception.app.DaoException;
import org.weather.exception.ErrorInfo;
import org.weather.exception.app.SessionNotFoundException;
import org.weather.model.Location;
import org.weather.model.Session;
import org.weather.model.User;
import org.weather.repository.LocationRepository;
import org.weather.repository.SessionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
public class LocationService {
    private final LocationRepository locationRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository, SessionRepository sessionRepository) {
        this.locationRepository = locationRepository;
        this.sessionRepository = sessionRepository;
    }

    @Transactional
    public void saveLocation(LocationToSaveDto locationDto) {
        log.info("Start saving location name = {}, lat = {}, lon = {}", locationDto.getLocationName(), locationDto.getLatitude(), locationDto.getLongitude());
        Optional<Session> sessionOptional = sessionRepository.findById(locationDto.getSessionId());
        Session session = sessionOptional.orElseThrow(() -> new SessionNotFoundException(ErrorInfo.SESSION_NOT_FOUND));
        User user = session.getUser();

        if (locationExists(user, locationDto)) {
            log.info("Location already exists. Skipping saving");
            return;
        }
        Location location = Location.builder()
                .name(locationDto.getLocationName())
                .user(user)
                .latitude(locationDto.getLatitude())
                .longitude(locationDto.getLongitude())
                .build();
        try {
            locationRepository.save(location);
        } catch (Exception e) {
            log.error("Failed to save location name = {}", locationDto.getLocationName(), e);
            throw new DaoException(ErrorInfo.DATA_SAVE_ERROR, e);
        }
        log.info("Location {} saved", locationDto.getLocationName());
    }

    public List<LocationSavedDto> findAllBySessionId(SessionIdDto sessionIdDto) {
        log.info("Getting locations for sessionId {}", sessionIdDto.getSessionId());
        User user = getUserBySession(sessionIdDto.getSessionId());

        List<Location> locations;
        try {
            locations = locationRepository.findAllByUser(user);
        } catch (Exception e) {
            log.error("Failed to fetch locations");
            throw new DaoException(ErrorInfo.DATA_FETCH_ERROR, e);
        }
        List<LocationSavedDto> locationSavedDtos = locations.stream()
                .map(this::mapToLocationSavedDto)
                .toList();

        log.info("Found {} locations for userId {}", locations.size(), user.getLogin());
        return locationSavedDtos;
    }

    @Transactional
    public void deleteLocation(LocationToDeleteDto location) {
        log.info("Deleting location lat = {}, lon = {} for sessionId {}", location.getLatitude(), location.getLongitude(), location.getSessionId());
        User user = getUserIdBySession(location.getSessionId());
        try {
            locationRepository.deleteByUserAndLatitudeAndLongitude(user, location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            log.error("Failed to delete location");
            throw new DaoException(ErrorInfo.DATA_DELETE_ERROR, e);
        }
        log.info("Deleted location lat = {}, lon = {}", location.getLatitude(), location.getLongitude());
    }

    private User getUserIdBySession(UUID sessionId) {
        Optional<Session> sessionOptional = sessionRepository.findById(sessionId);
        Session session = sessionOptional.orElseThrow(() -> new SessionNotFoundException(ErrorInfo.SESSION_NOT_FOUND));
        return session.getUser();
    }

    private LocationSavedDto mapToLocationSavedDto(Location location) {
        return LocationSavedDto.builder()
                .city(location.getName())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }

    private User getUserBySession(UUID sessionId) {
        Optional<Session> sessionOptional = sessionRepository.findById(sessionId);
        Session session = sessionOptional.orElseThrow(() -> new SessionNotFoundException(ErrorInfo.SESSION_NOT_FOUND));

        return session.getUser();
    }

    private boolean locationExists(User user, LocationToSaveDto locationDto) {
        try {
            return locationRepository.existsByUserAndNameAndLatitudeAndLongitude(
                    user,
                    locationDto.getLocationName(),
                    locationDto.getLatitude(),
                    locationDto.getLongitude());
        } catch (Exception e) {
            throw new DaoException(ErrorInfo.DATA_FETCH_ERROR, e);
        }
    }
}
