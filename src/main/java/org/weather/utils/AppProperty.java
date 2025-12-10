package org.weather.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class AppProperty {
    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.user}")
    private String dbUser;

    @Value("${db.password}")
    private String dbPassword;

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.base.url}")
    private String apiBaseUrl;

    @Value("${weather.api.geo.path}")
    private String apiGeocodingPath;

    @Value("${weather.api.weather.path}")
    private String apiWeatherPath;
}
