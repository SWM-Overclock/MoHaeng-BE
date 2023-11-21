package org.swmaestro.mohaeng.dto.location;

import lombok.Builder;
import lombok.Getter;
import org.swmaestro.mohaeng.domain.Location;

@Getter
public class LocationCreateResponseDto {

    private Long id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private boolean isPrimary;

    @Builder
    public LocationCreateResponseDto(Long id, String name, String address, double latitude, double longitude, boolean isPrimary) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isPrimary = isPrimary;
    }

    public static LocationCreateResponseDto of(Location location) {
    	return LocationCreateResponseDto.builder()
    			.id(location.getId())
                .name(location.getName())
                .address(location.getAddress())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .isPrimary(location.getIsPrimary())
    			.build();
    }
}
