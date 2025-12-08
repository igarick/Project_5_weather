package org.weather.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.weather.dto.SessionIdDto;
import org.weather.dto.UserIdDto;
import org.weather.exception.DaoException;
import org.weather.exception.ErrorInfo;
import org.weather.exception.SessionNotFoundException;
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
//        UUID sessionId = sessionIdDto.getSessionId();
        Optional<Session> sessionOptional = sessionRepository.findById(sessionIdDto.getSessionId());

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
    public SessionIdDto createSession(UserIdDto userId) {
        log.info("Creating session for user = {}", userId.getId());
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        OffsetDateTime dateTime = now.plusSeconds(sessionProperty.getSessionTimeout());

        Session session = new Session();
        UUID uuid = UUID.randomUUID();
        session.setId(uuid);
        session.setUser(User.builder()
                .id(userId.getId())
                .build());
        session.setExpiresAt(dateTime);

        try {
            sessionRepository.save(session);
        } catch (Exception e) {
            log.error("Failed to save session");
            throw new DaoException(ErrorInfo.DATA_SAVE_ERROR,e);
        }

        log.info("Session for user {} was successfully created", session.getUser().getId());
        return new SessionIdDto(session.getId());
    }

    @Transactional
    public void deactivateSession(SessionIdDto sessionIdDto) {
        log.info("Deactivating session = {}", sessionIdDto.getSessionId());
        Optional<Session> sessionOptional = sessionRepository.findById(sessionIdDto.getSessionId());
        Session session = sessionOptional.orElseThrow(() -> new SessionNotFoundException(ErrorInfo.SESSION_NOT_FOUND));

        session.setExpiresAt(OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(1));
    }
}
