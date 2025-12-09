package org.weather.exception.app;

import org.weather.exception.BaseException;
import org.weather.exception.ErrorInfo;

public class InvalidUserOrPasswordException extends BaseException {

    public InvalidUserOrPasswordException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public InvalidUserOrPasswordException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
