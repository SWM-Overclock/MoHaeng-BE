package org.swmaestro.mohaeng.domain.shop;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swmaestro.mohaeng.domain.BaseTimeEntity;
import org.swmaestro.mohaeng.domain.Status;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_detail_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @Column(name = "shop_name", nullable = false)
    private String name;

    @Column(name = "shop_address", nullable = false)
    private String address;

    @Column(name = "shop_latitude", nullable = false)
    private double latitude;

    @Column(name = "shop_longitude", nullable = false)
    private double longitude;

    @Column(name = "shop_phone", nullable = true)
    private String phone;

    @Column(name = "shop_image_url", nullable = true)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}
