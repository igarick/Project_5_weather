package org.weather.exception;

public class UnexpectedWeatherApiException extends BaseException {
    public UnexpectedWeatherApiException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public UnexpectedWeatherApiException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
