package org.weather.exception.app;

import org.weather.exception.BaseException;
import org.weather.exception.ErrorInfo;

public class DaoException extends BaseException {
    public DaoException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public DaoException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
