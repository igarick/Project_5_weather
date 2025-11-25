package org.weather.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class SessionProperty {

    @Value("${hibernate.sessionTimeout.seconds}")
    private int sessionTimeout;
}
