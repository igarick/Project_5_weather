package org.weather.exception.app;

import org.weather.exception.BaseException;
import org.weather.exception.ErrorInfo;

public class MappingException extends BaseException {
    public MappingException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public MappingException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
