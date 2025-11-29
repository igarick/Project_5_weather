package org.weather.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static jakarta.servlet.http.HttpServletResponse.*;

@AllArgsConstructor
@Getter
public enum ErrorInfo {
    LOGIN_DUPLICATE_ERROR("Account with this email already exists", SC_CONFLICT),
    USER_OR_PASSWORD_ERROR("Invalid username or password", SC_BAD_REQUEST),
    UNKNOWN_ERROR("Oops! Something Went Wrong. We're sorry, but an unexpected error has occurred. Please try again later", SC_INTERNAL_SERVER_ERROR),

    // from Api
    REQUEST_API_KEY_ERROR("Bad API key in API request for authorisation", SC_UNAUTHORIZED),
    REQUEST_API_ERROR("Bad request", SC_NOT_FOUND),
    FREQUENT_REQUEST_API_ERROR("Don't make more than 60 API calls per minute", 429),
    UNEXPECTED_API_ERROR("We're sorry, but an unexpected error has occurred. Please try again later", SC_INTERNAL_SERVER_ERROR),
    MAPPING_RESPONSE_API_ERROR("Mapping response API error"),



    END("--------------------");


    private final String message;
    private final int statusCode;

    ErrorInfo(String message) {
        this(message, 0);
    }
}

