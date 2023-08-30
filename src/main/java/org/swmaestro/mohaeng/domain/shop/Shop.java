package org.swmaestro.mohaeng.domain.shop;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swmaestro.mohaeng.domain.event.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ShopType shopType;

    @Column(name = "brand_code", nullable = false, length = 45)
    private String brandCode;

    @OneToMany(mappedBy = "shop")
    private List<Event> events = new ArrayList<>();

    @OneToMany(mappedBy = "shop")
    private List<ShopDetail> shopDetails = new ArrayList<>();

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public void addShopDetail(ShopDetail shopDetail) {
        this.shopDetails.add(shopDetail);
    }
}
