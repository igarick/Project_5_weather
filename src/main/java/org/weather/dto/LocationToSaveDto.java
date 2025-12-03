package org.weather.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class LocationToSaveDto {
    private UUID sessionId;

    @NotNull
    @NotBlank
    private String locationName;

    @NotNull
    private BigDecimal latitude;

    @NotNull
    private BigDecimal longitude;
}
