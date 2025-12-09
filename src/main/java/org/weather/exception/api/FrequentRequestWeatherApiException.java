package org.weather.exception.api;

import org.weather.exception.BaseException;
import org.weather.exception.ErrorInfo;

public class FrequentRequestWeatherApiException extends BaseException {
    public FrequentRequestWeatherApiException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public FrequentRequestWeatherApiException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
