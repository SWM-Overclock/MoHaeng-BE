package org.swmaestro.mohaeng.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LocationCreateResponseDto {

    private Long id;
    private String name;
    private String address;
    private String latitude;
    private String longitude;
    private boolean isUsed;

    @Builder
    public LocationCreateResponseDto(Long id, String name, String address, String latitude, String longitude, boolean isUsed) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isUsed = isUsed;
    }
}
