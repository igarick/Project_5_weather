package org.weather.exception.app;

import org.weather.exception.BaseException;
import org.weather.exception.ErrorInfo;

public class DeserializationException extends BaseException {
    public DeserializationException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public DeserializationException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
