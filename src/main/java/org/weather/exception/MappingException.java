package org.weather.exception;

public class MappingException extends BaseException {
    public MappingException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public MappingException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
