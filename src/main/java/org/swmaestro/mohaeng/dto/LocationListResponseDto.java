package org.swmaestro.mohaeng.dto;

import lombok.Getter;
import org.swmaestro.mohaeng.domain.Location;

@Getter
public class LocationListResponseDto {

    private Long id;
    private String name;
    private String address;
    private Boolean isUsed;

    public LocationListResponseDto(Long id, String name, String address, Boolean isUsed) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.isUsed = isUsed;
    }

    public static LocationListResponseDto of(Location location) {
        return new LocationListResponseDto(location.getId(), location.getName(), location.getAddress(), location.getIsUsed());
    }

}
