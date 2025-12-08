package org.weather.exception;

public class DeserializationException extends BaseException {
    public DeserializationException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public DeserializationException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
