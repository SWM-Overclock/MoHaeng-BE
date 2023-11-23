package org.swmaestro.mohaeng.dto.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LocationUpdateRequestDto {

    private String name;

    private String address;

    @DecimalMin(value = "33.0", message = "위도는 최소 33.0 이상이어야 합니다.")
    @DecimalMax(value = "38.0", message = "위도는 최대 38.0 이하여야 합니다.")
    private Double latitude;

    @DecimalMin(value = "124.0", message = "경도는 최소 124.0 이상이어야 합니다.")
    @DecimalMax(value = "132.0", message = "경도는 최대 132.0 이하여야 합니다.")
    private Double longitude;
}
