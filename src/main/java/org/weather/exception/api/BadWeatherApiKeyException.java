package org.weather.exception.api;

import org.weather.exception.BaseException;
import org.weather.exception.ErrorInfo;

public class BadWeatherApiKeyException extends BaseException {
    public BadWeatherApiKeyException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public BadWeatherApiKeyException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
