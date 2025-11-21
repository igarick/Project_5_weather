package org.weather.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.weather.config.SpringTestConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringTestConfig.class, UserService.class})
public class UserServiceTest {
}
