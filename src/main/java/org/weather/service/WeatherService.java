package org.weather.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WeatherService {
    private final HttpClient client = HttpClient.newHttpClient();

    public String getWeather() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openweathermap.org/data/2.5/weather?lat=51.5085&lon=-0.1257&appid=810674edcfe03956f3d710e75080d5c8"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String body = response.body();



        return body;
    }

    // поиск по названию
    // http://api.openweathermap.org/geo/1.0/direct?q=San Francisco&limit=10&appid=810674edcfe03956f3d710e75080d5c8
}
