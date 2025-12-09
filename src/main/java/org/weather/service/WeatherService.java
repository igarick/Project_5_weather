package org.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.weather.dto.location.LocationDto;
import org.weather.dto.location.LocationNameDto;
import org.weather.dto.weather.WeatherDto;
import org.weather.exception.*;
import org.weather.exception.api.BadRequestWeatherApiException;
import org.weather.exception.api.BadWeatherApiKeyException;
import org.weather.exception.api.FrequentRequestWeatherApiException;
import org.weather.exception.api.UnexpectedWeatherApiException;
import org.weather.exception.app.DeserializationException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
public class WeatherService {
    private final HttpClient client;
    private final JsonMapper jsonMapper;

    @Autowired
    public WeatherService(HttpClient client, JsonMapper jsonMapper) {
        this.client = client;
        this.jsonMapper = jsonMapper;
    }

    public List<LocationDto> getLocationByCityName(LocationNameDto locationNameDto) {
        log.info("Request by cityName = {} to API", locationNameDto.getLocalName());
        String localName = locationNameDto.getLocalName();
        String encodedCity = URLEncoder.encode(localName, StandardCharsets.UTF_8);
        String url = String.format("https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=10&appid=810674edcfe03956f3d710e75080d5c8", encodedCity);

        String body = getWeatherApiResponse(url);

        List<LocationDto> locations = null;
        try {
            locations = jsonMapper.readValue(body, new TypeReference<List<LocationDto>>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Mapping API error");
            throw new DeserializationException(ErrorInfo.MAPPING_RESPONSE_API_ERROR, e);
        }
        log.info("Found {} locations", locations.size());
        return locations;
    }

    public WeatherDto getWeatherByCoordinates(BigDecimal latitude, BigDecimal longitude) {          //String latitude, String longitude
        log.info("Weather request by lat = {} lon = {} to API", latitude, longitude);
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=810674edcfe03956f3d710e75080d5c8", latitude, longitude);
        String body = getWeatherApiResponse(url);

        WeatherDto weatherDto = null;
        try {
            weatherDto = jsonMapper.readValue(body, new TypeReference<WeatherDto>() {
            });
        } catch (JsonProcessingException e) {
            log.error("Mapping API error");
            throw new DeserializationException(ErrorInfo.MAPPING_RESPONSE_API_ERROR, e);
        }
        log.info("Found weather for city = {}", weatherDto.getCity());
        return weatherDto;
    }

    private String getWeatherApiResponse(String url) {
        log.info("API request by url = {}", url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error("Request failed, message = {}", e.getMessage());
            throw new UnexpectedWeatherApiException(ErrorInfo.UNEXPECTED_API_ERROR);
        }

        if (response.statusCode() != 200) {
            log.error("Request failed, status code = {}", response.statusCode());
            handleStatusCode(response.statusCode());
        }
        log.info("Successful Api response");
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
            default:
                throw new UnexpectedWeatherApiException(ErrorInfo.UNEXPECTED_API_ERROR);
        }
    }
}
