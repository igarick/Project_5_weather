package org.weather.service;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.weather.dto.location.LocationNameDto;
import org.weather.dto.location.LocationDto;
import org.weather.exception.api.UnexpectedWeatherApiException;
import org.weather.utils.AppProperty;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock
    HttpClient mockClient;

    @Mock
    HttpResponse<String> mockResponse;

    @Mock
    AppProperty appProperty;

    WeatherService weatherService;

    @BeforeEach
    void setup() {
        weatherService = new WeatherService(mockClient, new JsonMapper(), appProperty);

        when(appProperty.getApiBaseUrl()).thenReturn("https://api.openweathermap.org");
        when(appProperty.getApiWeatherPath()).thenReturn("/data/2.5/weather");
        when(appProperty.getApiGeocodingPath()).thenReturn("/geo/1.0/direct");
        when(appProperty.getApiKey()).thenReturn("810674edcfe03956f3d710e75080d5c8");

    }

    @Test
    void shouldFindLocationByCityName() throws IOException, InterruptedException {
        String jsonResponse = """
                        [   
                            {
                                "name": "San Francisco",
                                "lat": 8.2467485,
                                "lon": -80.9721357,
                                "country": "PA",
                                "state": "Veraguas"
                            }
                        ]
                """;
        when(mockResponse.body()).thenReturn(jsonResponse);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        LocationNameDto locationNameDto = new LocationNameDto("San Francisco");
        List<LocationDto> locations = weatherService.getLocationByCityName(locationNameDto);
        LocationDto city = locations.get(0);

        assertEquals("San Francisco", city.getName());
        assertEquals(new BigDecimal("8.2467485"), city.getLatitude());
        verify(mockClient, times(1)).send(any(), any());
    }

    @Test
    void invalidStatusCodeShouldThrowException() throws IOException, InterruptedException {
        when(mockResponse.statusCode()).thenReturn(500);
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);

        assertThrows(UnexpectedWeatherApiException.class,
                () -> weatherService.getWeatherByCoordinates(new BigDecimal("16.3721"), new BigDecimal("48.2085")));

    }
}
