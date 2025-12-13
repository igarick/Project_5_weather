package org.weather.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weather.dto.session.SessionIdDto;
import org.weather.dto.user.UserIdDto;
import org.weather.exception.app.DaoException;
import org.weather.exception.ErrorInfo;
import org.weather.exception.app.SessionNotFoundException;
import org.weather.model.Session;
import org.weather.model.User;
import org.weather.repository.SessionRepository;
import org.weather.utils.SessionProperty;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
public class SessionService {
    private final SessionRepository sessionRepository;
    private final SessionProperty sessionProperty;

    @Autowired
    public SessionService(SessionRepository sessionRepository, SessionProperty sessionProperty) {
        this.sessionRepository = sessionRepository;
        this.sessionProperty = sessionProperty;
    }

    public Optional<SessionIdDto> findCurrentSession(SessionIdDto sessionIdDto) {
        log.info("Searching for session by sessionId = {}", sessionIdDto.getSessionId());

        Optional<Session> sessionOptional;
        try {
            sessionOptional = sessionRepository.findById(sessionIdDto.getSessionId());
        } catch (Exception e) {
            throw new DaoException(ErrorInfo.DATA_FETCH_ERROR, e);
        }

        if (sessionOptional.isPresent() && !isExpired(sessionOptional.get())) {
            log.info("Received current session for user = {}", sessionOptional.get().getUser().getLogin());
            return Optional.of(new SessionIdDto(sessionOptional.get().getId()));
        }
        log.warn("Session is expired or absent");
        return Optional.empty();
    }

    private boolean isExpired(Session session) {
        return session.getExpiresAt().isBefore(OffsetDateTime.now(ZoneOffset.UTC));
    }

    @Transactional
    public SessionIdDto createSession(UserIdDto userIdDto) {
        log.info("Creating session for user = {}", userIdDto.getId());
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime expirationTime = now.plusSeconds(sessionProperty.getSessionTimeout());

        UUID uuid = UUID.randomUUID();
        Session session = Session.builder()
                .id(uuid)
                .user(User.builder()
                        .id(userIdDto.getId())
                        .build())
                .expiresAt(expirationTime)
                .build();
        try {
            sessionRepository.save(session);
        } catch (Exception e) {
            log.error("Failed to save session for user = {}", userIdDto.getId());
            throw new DaoException(ErrorInfo.DATA_SAVE_ERROR, e);
        }
        log.info("Session for user {} was successfully created", session.getUser().getId());
        return new SessionIdDto(session.getId());
    }

    @Transactional
    public void deactivateSession(SessionIdDto sessionIdDto) {
        log.info("Deactivating session = {}", sessionIdDto.getSessionId());
        Session session = getSession(sessionIdDto.getSessionId());
        session.setExpiresAt(OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(1));
    }

    public String getLoginById(UUID sessionId) {
        Session session = getSession(sessionId);
        return session.getUser().getLogin();
    }

    private Session getSession(UUID sessionId) {
        Optional<Session> sessionOptional;
        try {
            sessionOptional = sessionRepository.findById(sessionId);
        } catch (Exception e) {
            log.error("Failed to fetch session by sessionId = {}", sessionId);
            throw new DaoException(ErrorInfo.DATA_FETCH_ERROR, e);
        }
        return sessionOptional.orElseThrow(() -> new SessionNotFoundException(ErrorInfo.SESSION_NOT_FOUND));
    }
}
