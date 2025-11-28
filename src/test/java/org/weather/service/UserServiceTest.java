package org.weather.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.weather.config.SpringTestConfig;
import org.weather.dto.InputUserRegistrationDto;
import org.weather.exception.DuplicateUserException;
import org.weather.model.User;
import org.weather.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
//@WebAppConfiguration
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveUser_shouldPersistUser() {
        InputUserRegistrationDto dto = new InputUserRegistrationDto("login", "password", "password");

        userService.registerUser(dto);

        Optional<User> userOptional = userRepository.findByLogin(dto.getLogin());
        assertThat(userOptional).isPresent();
        userOptional.ifPresent(user -> assertThat(user.getLogin()).isEqualTo(dto.getLogin()));
    }

    @Test
    void duplicateUserShouldThrowException() {
        InputUserRegistrationDto dto = new InputUserRegistrationDto("login", "password", "password");

        userService.registerUser(dto);
        assertThrows(DuplicateUserException.class,
                () -> userService.registerUser(dto));
    }
}
