package org.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.weather.model.Session;
import org.weather.repository.SessionRepository;

import java.util.Optional;

@Service
public class SessionService {
    private final SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void getSession(Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findById(userId);
    }
}
