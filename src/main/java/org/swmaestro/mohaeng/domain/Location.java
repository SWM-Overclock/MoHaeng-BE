package org.swmaestro.mohaeng.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swmaestro.mohaeng.domain.user.User;

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
    private String latitude;

    @Column(name = "longitude", nullable = false)
    private String longitude;

    @Column(name = "is_used", nullable = false)
    private Boolean isUsed;

    @Builder
    public Location(User user, String name, String address, String latitude, String longitude, Boolean isUsed) {
        this.user = user;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isUsed = isUsed;
    }

    public static Location of(User user, String name, String address, String latitude, String longitude, boolean isUsed) {
        return Location.builder()
                .user(user)
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .isUsed(isUsed)
                .build();
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
}
