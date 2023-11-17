package org.swmaestro.mohaeng.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.swmaestro.mohaeng.domain.Location;

@Getter
@AllArgsConstructor
public class LocationDetailResponseDto {

    private Long id;
    private String name;
    private String address;
    private String latitude;
    private String longitude;
    private boolean isUsed;

    public static LocationDetailResponseDto of(Location location) {
        return new LocationDetailResponseDto(location.getId(),
                location.getName(),
                location.getAddress(),
                location.getLatitude(),
                location.getLongitude(),
                location.getIsUsed());
    }
}
