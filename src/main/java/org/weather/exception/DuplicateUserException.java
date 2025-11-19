package org.weather.exception;

public class DuplicateUserException extends BaseException {

    public DuplicateUserException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
