package org.weather.exception;

public class InvalidUserOrPasswordException extends BaseException {

    public InvalidUserOrPasswordException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public InvalidUserOrPasswordException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
