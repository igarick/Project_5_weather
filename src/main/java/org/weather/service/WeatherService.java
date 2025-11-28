package org.weather.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.weather.dto.LocationDto;
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
    //    private final HttpClient client = HttpClient.newHttpClient();
    private final HttpClient client;

    private final JsonMapper jsonMapper;

    @Autowired
    public WeatherService(HttpClient client, JsonMapper jsonMapper) {
        this.client = client;
        this.jsonMapper = jsonMapper;
    }

    public List<LocationDto> getLocationByCityName(String city) throws IOException, InterruptedException {
        String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        String url = String.format("https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=10&appid=810674edcfe03956f3d710e75080d5c8", encodedCity);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            handleStatusCode(response.statusCode());
        }

        String body = response.body();

        List<LocationDto> locations = jsonMapper.readValue(body, new TypeReference<List<LocationDto>>() {});

        return locations;
    }

    public String getWeatherByCoordinates(String latitude, String longitude) throws IOException, InterruptedException {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=810674edcfe03956f3d710e75080d5c8", latitude, longitude);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            handleStatusCode(response.statusCode());
        }

        String body = response.body();



        return body;
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
