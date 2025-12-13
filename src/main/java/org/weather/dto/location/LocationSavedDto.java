package org.weather.dto.location;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class LocationSavedDto {
    private String city;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
