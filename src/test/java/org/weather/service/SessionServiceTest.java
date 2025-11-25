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
import org.weather.dto.InputUserRegistrationDto;
import org.weather.dto.SessionIdDto;
import org.weather.dto.UserIdDto;
import org.weather.model.User;
import org.weather.repository.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
        userService.registerUser(new InputUserRegistrationDto("login", "password", "password"));
        Optional<User> byLogin = userRepository.findByLogin("login");
        Long id = byLogin.get().getId();
        UserIdDto userIdDto = new UserIdDto(id);


        UUID sessionIdParam = UUID.randomUUID();
        String sessionId = sessionIdParam.toString();
        SessionIdDto session = sessionService.getSession(userIdDto, sessionId);
        UUID firstId = session.getSessionId();
        Thread.sleep(1100);

        SessionIdDto newSession = sessionService.getSession(userIdDto, firstId.toString());
        UUID secondId = newSession.getSessionId();

        assertNotEquals(firstId, secondId);
    }
}
