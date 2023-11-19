package org.swmaestro.mohaeng.dto.location;

import lombok.Getter;
import org.swmaestro.mohaeng.domain.Location;

@Getter
public class LocationListResponseDto {

    private Long id;
    private String name;
    private String address;
    private Boolean isPrimary;

    public LocationListResponseDto(Long id, String name, String address, Boolean isPrimary) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.isPrimary = isPrimary;
    }

    public static LocationListResponseDto of(Location location) {
        return new LocationListResponseDto(location.getId(), location.getName(), location.getAddress(), location.getIsPrimary());
    }

}
