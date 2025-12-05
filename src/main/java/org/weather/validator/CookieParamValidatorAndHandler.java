package org.weather.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.weather.dto.SessionIdDto;
import org.weather.exception.SessionNotFoundException;
import org.weather.exception.ErrorInfo;
import org.weather.service.SessionService;

import java.util.Optional;
import java.util.UUID;

@Component
public class CookieParamValidatorAndHandler {
    private final Logger log = LoggerFactory.getLogger(CookieParamValidatorAndHandler.class);

    private final SessionService sessionService;

    @Autowired
    public CookieParamValidatorAndHandler(SessionService sessionService) {
        this.sessionService = sessionService;
    }

//    public SessionIdDto getCurrentSession(String sessionIdParam) {
//        UUID sessionId = extractSessionId(sessionIdParam);
//        Optional<SessionIdDto> currentSession = sessionService.findCurrentSession(new SessionIdDto(sessionId));
//        if (currentSession.isEmpty()) {
//            throw new SessionNotFoundException(ErrorInfo.SESSION_NOT_FOUND);
//        }
//        return currentSession.get();
//    }


    public void validateSessionExists(UUID sessionId) {
        Optional<SessionIdDto> currentSession = sessionService.findCurrentSession(new SessionIdDto(sessionId));
        if (currentSession.isEmpty()) {
            log.warn("Session not found");
            throw new SessionNotFoundException(ErrorInfo.SESSION_NOT_FOUND);
        }
    }

    public SessionIdDto getCurrentSession(SessionIdDto sessionIdDto) {
        Optional<SessionIdDto> currentSession = sessionService.findCurrentSession(sessionIdDto);
        return currentSession.orElseThrow(() -> new SessionNotFoundException(ErrorInfo.SESSION_NOT_FOUND));
    }



    public UUID extractSessionId(String sessionIdParam) {
        if (!StringUtils.hasText(sessionIdParam)) {
            log.warn("Session not found");
            throw new SessionNotFoundException(ErrorInfo.SESSION_NOT_FOUND);
        }

        try {
            return UUID.fromString(sessionIdParam);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid session id");
            throw new SessionNotFoundException(ErrorInfo.INVALID_SESSION);
        }
    }
}
