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

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {
    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public SessionIdDto getSession(UserIdDto userId, String sessionIdParam) {
        log.info("Начат процесс получения/создания сессии для {}", userId.getId());

        UUID sessionIdDto;
        if (sessionIdParam.isEmpty()) {
            Session session = createSession(userId);
            sessionIdDto = session.getId();
            log.info("Сессия без входного UUID создана для пользователя {}", session.getUser());
        } else {
            UUID sessionId = UUID.fromString(sessionIdParam);
            Optional<Session> sessionOptional = sessionRepository.findById(sessionId);
            Session session = sessionOptional.orElseThrow(() ->
            {
                log.error("Ошибка получения сессии из репозитория по UUID");
                return new UnknownException(ErrorInfo.UNKNOWN_ERROR);
            });

            sessionIdDto = session.getId();
            log.info("Получение сессии {} из репозитория по UUID", session);
        }
        return new SessionIdDto(sessionIdDto);
    }

    private Session createSession(UserIdDto userId) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime dateTime = now.plusSeconds(60);

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
