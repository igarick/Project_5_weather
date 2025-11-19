package org.weather.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;

@AllArgsConstructor
@Getter
public enum ErrorInfo {
    LOGIN_DUPLICATE_ERROR("Account with this email already exists", SC_CONFLICT),
    USER_OR_PASSWORD_ERROR("Invalid username or password", SC_BAD_REQUEST),


    END("--------------------");


    private final String message;
    private final int statusCode;

    ErrorInfo(String message) {
        this(message, 0);
    }
}

