package org.weather.exception.app;

import org.weather.exception.BaseException;
import org.weather.exception.ErrorInfo;

public class DuplicateUserException extends BaseException {

    public DuplicateUserException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
