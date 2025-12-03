package org.weather.service;

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
import org.weather.model.Session;
import org.weather.model.User;
import org.weather.repository.SessionRepository;
import org.weather.utils.SessionProperty;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class SessionService {
    private static final Logger log = LoggerFactory.getLogger(SessionService.class);

    private final SessionRepository sessionRepository;
    private final SessionProperty sessionProperty;

    @Autowired
    public SessionService(SessionRepository sessionRepository, SessionProperty sessionProperty) {
        this.sessionRepository = sessionRepository;
        this.sessionProperty = sessionProperty;
    }

    public Optional<SessionIdDto> findCurrentSession(SessionIdDto sessionIdDto) {
        UUID sessionId = sessionIdDto.getSessionId();
        log.info("Начат процесс поиска сессии для UUID = {}", sessionId.toString());

        Optional<Session> sessionOptional = sessionRepository.findById(sessionId);

        if (sessionOptional.isPresent() && !isExpired(sessionOptional.get())) {
            log.info("Получена действующая сессия для пользователя {}", sessionOptional.get().getUser().getLogin());
            return Optional.of(new SessionIdDto(sessionOptional.get().getId()));
        }

        log.warn("Сессия для данного пользователя истекла или ее нет");
        return Optional.empty();
    }

    private boolean isExpired(Session session) {
        return session.getExpiresAt().isBefore(OffsetDateTime.now());
    }

    @Transactional
    public SessionIdDto createSession(UserIdDto userId) {
        log.info("Начало создание сессии для пользователя {}", userId.getId());
        OffsetDateTime now = OffsetDateTime.now();
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

        log.info("Session for user {} was successfully created", session.getUser());
        return new SessionIdDto(session.getId());
    }

//    private Session createSession(UserIdDto userId) {
//        OffsetDateTime now = OffsetDateTime.now();
//        OffsetDateTime dateTime = now.plusSeconds(sessionProperty.getSessionTimeout());
//
//        Session session = new Session();
//        UUID uuid = UUID.randomUUID();
//        session.setId(uuid);
//        session.setUser(User.builder()
//                .id(userId.getId())
//                .build());
//        session.setExpiresAt(dateTime);
//
//        sessionRepository.save(session);
//
//        return session;
//    }

}
