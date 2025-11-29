package org.weather.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class SerializationOrDeserializationException extends BaseException {
    public SerializationOrDeserializationException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public SerializationOrDeserializationException(ErrorInfo errorInfo, Throwable cause) {
        super(errorInfo, cause);
    }
}
