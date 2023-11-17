package org.swmaestro.mohaeng.domain.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swmaestro.mohaeng.domain.BaseTimeEntity;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.Role;
import org.swmaestro.mohaeng.domain.Status;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "`user`")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique=true)
    private String email;

    @Column(name = "nick_name", nullable = false)
    private String nickName;

    @Column(nullable = false)
    private String provider;

    @Column(name = "provider_id", nullable = false)
    private String providerId;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private List<Location> locations = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Builder
    public User(Long id, String email, String nickName, String provider, String providerId, String imageUrl, Status status, Role role) {
        this.id = id;
        this.email = email;
        this.nickName = nickName;
        this.provider = provider;
        this.providerId = providerId;
        this.imageUrl = imageUrl;
        this.status = status;
        this.role = role;
    }

    public static User createUser(String email, String nickname, String provider, String providerId, String imageUrl) {
        return User.builder()
                .email(email)
                .nickName(nickname)
                .provider(provider)
                .providerId(providerId)
                .imageUrl(imageUrl)
                .status(Status.ACTIVE)
                .role(Role.USER)
                .build();
    }

    public String getRoleKey() {
        return this.role.getKey();
    }

    public void addLocation(Location location) {
        this.locations.add(location);
    }
  
    public void updateProfileImage(String updatedImageUrl) {
        this.imageUrl = updatedImageUrl;
    }
}
