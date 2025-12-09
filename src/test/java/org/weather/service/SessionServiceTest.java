package org.weather.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.weather.config.SpringTestConfig;
import org.weather.dto.user.UserRegistrationDto;
import org.weather.dto.session.SessionIdDto;
import org.weather.dto.user.UserIdDto;
import org.weather.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
@WebAppConfiguration
@Transactional
public class SessionServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionService sessionService;

    @Test
    void userSessionShouldBeExpired() throws InterruptedException {
        userService.registerUser(new UserRegistrationDto("login", "password", "password"));
        Long userId = userRepository.findByLogin("login").get().getId();

        SessionIdDto sessionIdDto = sessionService.createSession(new UserIdDto(userId));

        Thread.sleep(1100);

        Optional<SessionIdDto> currentSession = sessionService.findCurrentSession(sessionIdDto);
        assertTrue(currentSession.isEmpty());
    }
}
