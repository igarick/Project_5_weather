package org.weather.exception;

public class SessionNotFoundException extends BaseException {

    public SessionNotFoundException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public SessionNotFoundException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
