package com.highfive.meetu.domain.job.personal.dto;

import com.highfive.meetu.domain.job.common.entity.Location;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {
    private Long id;
    private String locationCode;
    private String province;
    private String city;
    private String fullLocation;

    public static LocationDTO fromEntity(Location location) {
        return LocationDTO.builder()
                .locationCode(location.getLocationCode())
                .province(location.getProvince())
                .city(location.getCity())
                .fullLocation(location.getFullLocation())
                .build();
    }
}
