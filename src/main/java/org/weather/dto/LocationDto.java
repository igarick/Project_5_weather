package org.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties("local_names")
public class LocationDto {
    @JsonProperty("name")
    String name;
    @JsonProperty("lat")
    BigDecimal latitude;
    @JsonProperty("lon")
    BigDecimal longitude;
    @JsonProperty("country")
    String country;
    @JsonProperty("state")
    String state = "none";


    // вместо null при десериализации в дто
    //    String state = "none";


}
