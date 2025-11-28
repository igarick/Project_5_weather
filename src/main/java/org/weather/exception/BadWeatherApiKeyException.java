package org.weather.exception;

public class BadWeatherApiKeyException extends BaseException {
    public BadWeatherApiKeyException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public BadWeatherApiKeyException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
