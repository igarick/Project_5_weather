package org.weather.service;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.weather.dto.InputUserLoginDto;
import org.weather.dto.InputUserRegistrationDto;
import org.weather.dto.UserIdDto;
import org.weather.exception.DaoException;
import org.weather.exception.DuplicateUserException;
import org.weather.exception.ErrorInfo;
import org.weather.exception.InvalidUserOrPasswordException;
import org.weather.model.User;
import org.weather.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void registerUser(InputUserRegistrationDto userDto) { //String login, String password
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
            throw new DaoException(ErrorInfo.DATA_SAVE_ERROR,e);
        }
        log.info("User = {} saved", userDto.getLogin());
    }

    public UserIdDto authenticateUser(InputUserLoginDto userDto) {
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
