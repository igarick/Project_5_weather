package org.weather.exception.api;

import org.weather.exception.BaseException;
import org.weather.exception.ErrorInfo;

public class BadRequestWeatherApiException extends BaseException {
    public BadRequestWeatherApiException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public BadRequestWeatherApiException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
