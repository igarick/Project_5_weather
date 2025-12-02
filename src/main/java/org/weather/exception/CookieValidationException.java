package org.weather.exception;

public class CookieValidationException extends BaseException {

    public CookieValidationException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public CookieValidationException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
