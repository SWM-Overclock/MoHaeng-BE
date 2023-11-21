package org.swmaestro.mohaeng.dto.location;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.user.User;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationCreateRequestDto {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    @DecimalMin(value = "33.0", message = "위도는 최소 33.0 이상이어야 합니다.")
    @DecimalMax(value = "38.0", message = "위도는 최대 38.0 이하여야 합니다.")
    private double latitude;

    @DecimalMin(value = "124.0", message = "경도는 최소 124.0 이상이어야 합니다.")
    @DecimalMax(value = "132.0", message = "경도는 최대 132.0 이하여야 합니다.")
    private double longitude;

    public Location toEntity(User user) {
        return Location.of(user, name, address, latitude, longitude, true);
    }
}
