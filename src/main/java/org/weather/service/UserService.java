package org.weather.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weather.dto.UserRegistrationDto;
import org.weather.model.User;
import org.weather.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void registerUser(String name, String password) {
        User user = User.builder()
                .login(name)
                .password(password)
                .build();
        userRepository.save(user);
    }
}
