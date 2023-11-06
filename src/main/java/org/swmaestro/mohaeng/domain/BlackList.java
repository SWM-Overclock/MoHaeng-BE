package org.swmaestro.mohaeng.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swmaestro.mohaeng.domain.user.User;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlackList extends BaseTimeEntity {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "blocked_feature", nullable = false)
    private String blockedFeature;

    @Column(name = "blocked_reason", nullable = false)
    private String blockedReason;
}
