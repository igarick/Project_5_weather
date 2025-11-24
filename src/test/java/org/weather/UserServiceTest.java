package org.weather;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.weather.config.SpringTestConfig;
import org.weather.dto.InputUserRegistrationDto;
import org.weather.exception.DuplicateUserException;
import org.weather.model.User;
import org.weather.repository.UserRepository;
import org.weather.service.UserService;

import javax.sql.DataSource;                   // интерфейс DataSource

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

//@ActiveProfiles("test")
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = SpringTestConfig.class)
//@ComponentScan({
//        "org.weather.service",
//        "org.weather.repository",

/// /        "org.weather.model"
//})
//@WebAppConfiguration


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringTestConfig.class)
@WebAppConfiguration
class UserServiceTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataSource dataSource;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() throws Exception {
        userService.deleteAllUsers();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

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
