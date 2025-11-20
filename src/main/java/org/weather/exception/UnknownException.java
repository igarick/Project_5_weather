package org.weather.exception;

public class UnknownException extends BaseException {
    public UnknownException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public UnknownException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
