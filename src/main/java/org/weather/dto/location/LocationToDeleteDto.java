package org.weather.dto.location;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class LocationToDeleteDto {
    private UUID sessionId;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
