package org.swmaestro.mohaeng.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.user.User;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationCreateRequestDto {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "위도를 입력해주세요.")
    private String latitude;

    @NotBlank(message = "경도를 입력해주세요.")
    private String longitude;

    public Location toEntity(User user) {
        return Location.builder()
                .user(user)
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .isUsed(false)
                .build();
    }
}
