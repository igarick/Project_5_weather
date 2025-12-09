package org.weather.exception.app;

import org.weather.exception.BaseException;
import org.weather.exception.ErrorInfo;

public class SessionNotFoundException extends BaseException {

    public SessionNotFoundException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public SessionNotFoundException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
