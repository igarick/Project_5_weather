package org.weather.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.weather.dto.SessionIdDto;
import org.weather.dto.UserIdDto;
import org.weather.exception.ErrorInfo;
import org.weather.exception.UnknownException;
import org.weather.model.Session;
import org.weather.model.User;
import org.weather.repository.SessionRepository;
import org.weather.utils.SessionProperty;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {
    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;
    private final SessionProperty sessionProperty;

    @Autowired
    public SessionService(SessionRepository sessionRepository, SessionProperty sessionProperty) {
        this.sessionRepository = sessionRepository;
        this.sessionProperty = sessionProperty;
    }

    public SessionIdDto getSession(UserIdDto userId, String sessionIdParam) {
        log.info("Начат процесс получения/создания сессии для {}", userId.getId());

        UUID sessionIdDto;
        if (sessionIdParam.isEmpty()) {
            Session session = createSession(userId);
            sessionIdDto = session.getId();
            log.info("Создана новая сессия для пользователя {}", session.getUser());
        } else {
            UUID sessionId = UUID.fromString(sessionIdParam);
            Optional<Session> sessionOptional = sessionRepository.findById(sessionId);
            if (sessionOptional.isPresent() && !isExpired(sessionOptional.get())) {
                sessionIdDto = sessionOptional.get().getId();
                log.info("Получена действующая сессия для пользователя {}", sessionOptional.get().getUser());
            } else {
                Session session = createSession(userId);
                sessionIdDto = session.getId();
                log.info("Старая сессия отсутствует/истекала. Создана новая сессия для пользователя {}", session.getUser());
            }
        }
        return new SessionIdDto(sessionIdDto);
    }

    private boolean isExpired(Session session) {
        return session.getExpiresAt().isBefore(OffsetDateTime.now());
    }

    private Session createSession(UserIdDto userId) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime dateTime = now.plusSeconds(sessionProperty.getSessionTimeout());

        Session session = new Session();
        UUID uuid = UUID.randomUUID();
        session.setId(uuid);
        session.setUser(User.builder()
                .id(userId.getId())
                .build());
        session.setExpiresAt(dateTime);

        sessionRepository.save(session);

        return session;
    }


}
