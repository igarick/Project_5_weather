package org.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.weather.dto.LocationNameDto;
import org.weather.dto.LocationDto;
import org.weather.dto.WeatherDto;
import org.weather.exception.*;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class WeatherService {
    private final Logger log = LoggerFactory.getLogger(WeatherService.class);

    private final HttpClient client;
    private final JsonMapper jsonMapper;

    @Autowired
    public WeatherService(HttpClient client, JsonMapper jsonMapper) {
        this.client = client;
        this.jsonMapper = jsonMapper;
    }

    public List<LocationDto> getLocationByCityName(LocationNameDto locationNameDto) {
        log.info("Формирование запроса для получения локации по городу {} к API", locationNameDto);
        String localName = locationNameDto.getLocalName();
        String encodedCity = URLEncoder.encode(localName, StandardCharsets.UTF_8);
        String url = String.format("https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=10&appid=810674edcfe03956f3d710e75080d5c8", encodedCity);

        String body = getWeatherApiResponse(url);

        List<LocationDto> locations = null;
        try {
            locations = jsonMapper.readValue(body, new TypeReference<List<LocationDto>>() {});
        } catch (JsonProcessingException e) {
            log.error("Ошибка при десериализации");
            throw new SerializationOrDeserializationException(ErrorInfo.MAPPING_RESPONSE_API_ERROR, e);
        }
        log.info("Получены локация для {}", localName);
        return locations;
    }

    public WeatherDto getWeatherByCoordinates(String latitude, String longitude) {
        log.info("Формирование запроса для получения погоды по координатам lat = {} lon = {} к API", latitude, longitude);
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=810674edcfe03956f3d710e75080d5c8", latitude, longitude);
        String body = getWeatherApiResponse(url);

        WeatherDto weatherDto = null;
        try {
            weatherDto = jsonMapper.readValue(body, new TypeReference<WeatherDto>() {});
        } catch (JsonProcessingException e) {
            log.error("Ошибка при десериализации");
            throw new SerializationOrDeserializationException(ErrorInfo.MAPPING_RESPONSE_API_ERROR, e);
        }
        log.info("Получены погода по координатам lat = {} lon = {} к API", latitude, longitude);
        return weatherDto;
    }

    private String getWeatherApiResponse(String url) {
        log.info("Запрос к API по url = {}", url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.warn("Ошибка при обращение к API, {}", e.getMessage());
            throw new UnexpectedWeatherApiException(ErrorInfo.UNEXPECTED_API_ERROR);
        }

        if (response.statusCode() != 200) {
            log.warn("Ответ API с ошибкой - {}", response.statusCode());
            handleStatusCode(response.statusCode());
        }
        log.info("Успешный ответ от API");
        return response.body();
    }

    private void handleStatusCode(int code) {
        switch (code) {
            case 401:
                throw new BadWeatherApiKeyException(ErrorInfo.REQUEST_API_KEY_ERROR);
            case 404:
                throw new BadRequestWeatherApiException(ErrorInfo.REQUEST_API_ERROR);
            case 429:
                throw new FrequentRequestWeatherApiException(ErrorInfo.FREQUENT_REQUEST_API_ERROR);
            default: throw new UnexpectedWeatherApiException(ErrorInfo.UNEXPECTED_API_ERROR);
        }
    }
}
