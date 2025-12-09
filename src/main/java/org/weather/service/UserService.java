package org.weather.service;

import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weather.dto.user.UserLoginDto;
import org.weather.dto.user.UserRegistrationDto;
import org.weather.dto.user.UserIdDto;
import org.weather.exception.app.DaoException;
import org.weather.exception.app.DuplicateUserException;
import org.weather.exception.ErrorInfo;
import org.weather.exception.app.InvalidUserOrPasswordException;
import org.weather.model.User;
import org.weather.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void registerUser(UserRegistrationDto userDto) { //String login, String password
        String hashedPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());

        User user = User.builder()
                .login(userDto.getLogin())
                .password(hashedPassword)
                .build();

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.warn("Failed to save user {}. User already exists", userDto.getLogin());
            throw new DuplicateUserException(ErrorInfo.LOGIN_DUPLICATE_ERROR, e);
        } catch (Exception e) {
            log.warn("Failed to save user {}", userDto.getLogin());
            throw new DaoException(ErrorInfo.DATA_SAVE_ERROR, e);
        }
        log.info("User = {} saved", userDto.getLogin());
    }

    public UserIdDto authenticateUser(UserLoginDto userDto) {
        Optional<User> optionalUser = userRepository.findByLogin((userDto.getLogin()));
        User user = optionalUser.orElseThrow(() -> {
            log.warn("User {} not found", userDto.getLogin());
            return new InvalidUserOrPasswordException(ErrorInfo.USER_OR_PASSWORD_ERROR);
        });

        boolean passwordMatches = BCrypt.checkpw(userDto.getPassword(), user.getPassword());
        if (!passwordMatches) {
            log.warn("Invalid password for user {}", userDto.getLogin());
            throw new InvalidUserOrPasswordException(ErrorInfo.USER_OR_PASSWORD_ERROR);
        }

        log.info("User {} successfully authenticated", userDto.getLogin());
        return new UserIdDto(user.getId());
    }
}
