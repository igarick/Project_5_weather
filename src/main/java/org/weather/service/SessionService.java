package org.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.weather.dto.UserIdDto;
import org.weather.model.Session;
import org.weather.repository.SessionRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void getSession(UserIdDto userId) {
        Long id = userId.getId();
        Optional<Session> sessionOptional = sessionRepository.findById(id);

        Session session = new Session();
        OffsetDateTime time = OffsetDateTime.now();
        OffsetDateTime dateTime = time.plusSeconds(30);

        if (sessionOptional.isEmpty()) {
            UUID uuid = UUID.randomUUID();
            session.setId(uuid);
            session.setExpiresAt(dateTime);
        }
    }
}
