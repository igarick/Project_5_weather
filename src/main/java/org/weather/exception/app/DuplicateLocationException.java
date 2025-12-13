package org.weather.exception.app;

import org.weather.exception.BaseException;
import org.weather.exception.ErrorInfo;

public class DuplicateLocationException extends BaseException {
    public DuplicateLocationException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public DuplicateLocationException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
