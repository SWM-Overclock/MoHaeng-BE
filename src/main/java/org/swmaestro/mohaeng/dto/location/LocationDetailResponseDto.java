package org.swmaestro.mohaeng.dto.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.swmaestro.mohaeng.domain.Location;

@Getter
@AllArgsConstructor
public class LocationDetailResponseDto {

    private Long id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private boolean isPrimary;

    public static LocationDetailResponseDto of(Location location) {
        return new LocationDetailResponseDto(location.getId(),
                location.getName(),
                location.getAddress(),
                location.getLatitude(),
                location.getLongitude(),
                location.getIsPrimary());
    }
}
