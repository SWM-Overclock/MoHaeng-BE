package org.swmaestro.mohaeng.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.dto.location.LocationUpdateRequestDto;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Location extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "is_primary", nullable = false)
    private Boolean isPrimary;

    @Builder
    public Location(User user, String name, String address, Double latitude, Double longitude, Boolean isPrimary) {
        this.user = user;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isPrimary = isPrimary;
    }

    public static Location of(User user, String name, String address, Double latitude, Double longitude, Boolean isPrimary) {
        return Location.builder()
                .user(user)
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .isPrimary(isPrimary)
                .build();
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    public boolean isPrimary() {
        return this.isPrimary;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void update(LocationUpdateRequestDto updateRequestDto) {
        if (updateRequestDto.getName() != null) {
            this.name = updateRequestDto.getName();
        }
        if (updateRequestDto.getAddress() != null) {
            this.address = updateRequestDto.getAddress();
        }
        if (updateRequestDto.getLatitude() != null) {
            this.latitude = updateRequestDto.getLatitude();
        }
        if (updateRequestDto.getLongitude() != null) {
            this.longitude = updateRequestDto.getLongitude();
        }
    }
}
