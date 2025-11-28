package org.weather;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.weather.dto.LocationDto;
import org.weather.service.WeatherService;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;

public class main {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        JsonMapper jsonMapper = new JsonMapper();

        WeatherService weatherService = new WeatherService(httpClient, jsonMapper);

//        String city = "San Francisco";
        String city = "1";
        List<LocationDto> locationDtos = weatherService.getLocationByCityName(city);

        for (LocationDto locationDto : locationDtos) {
            System.out.println(locationDto);

        }


//        System.out.println("");
//
//        String lat = "37.7749";
//        String lon = "-122.4194";
//        String weatherByCoordinates = weatherService.getWeatherByCoordinates(lat, lon);
//        System.out.println(weatherByCoordinates);
    }

}
