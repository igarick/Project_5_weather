package org.weather.exception;

public class BadRequestWeatherApiException extends BaseException {
    public BadRequestWeatherApiException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public BadRequestWeatherApiException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
