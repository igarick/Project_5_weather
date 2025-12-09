package org.weather.exception.api;

import org.weather.exception.BaseException;
import org.weather.exception.ErrorInfo;

public class UnexpectedWeatherApiException extends BaseException {
    public UnexpectedWeatherApiException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public UnexpectedWeatherApiException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
