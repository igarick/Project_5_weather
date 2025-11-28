package org.weather.exception;

public class FrequentRequestWeatherApiException extends BaseException {
    public FrequentRequestWeatherApiException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public FrequentRequestWeatherApiException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
