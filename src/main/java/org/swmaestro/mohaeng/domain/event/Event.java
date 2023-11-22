package org.swmaestro.mohaeng.domain.event;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.swmaestro.mohaeng.domain.BaseTimeEntity;
import org.swmaestro.mohaeng.domain.Status;
import org.swmaestro.mohaeng.domain.shop.Shop;
import org.swmaestro.mohaeng.domain.View;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    @OneToMany(mappedBy = "event")
    private List<View> views = new ArrayList<>();

    @Column(name = "category_code", nullable = false, length = 45)
    private String categoryCode;

    @Convert(converter = EventTypeConverter.class)
    private EventType eventType;

    @Column(name = "event_regular_price", nullable = false)
    private int regularPrice;

    @Column(name = "event_discount_price")
    private int discountPrice;

    @Column(name = "event_name", nullable = false)
    private String name;

    @Column(name = "event_description", nullable = false)
    private String description;

    @Column(name = "event_image_url", nullable = false)
    private String imageUrl;

    @Column(name = "event_start_date", nullable = false)
    private String startDate;

    @Column(name = "event_end_date", nullable = false)
    private String endDate;

    @Column(name = "event_start_time")
    private String startTime;

    @Column(name = "event_end_time")
    private String endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_created_by", nullable = false)
    private CreatedBy createdBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_status", nullable = false)
    private Status status;

    @Builder
    public Event(Shop shop, String categoryCode, EventType eventType, int regularPrice, int discountPrice, String name, String description, String imageUrl, String startDate, String endDate, String startTime, String endTime, CreatedBy createdBy, Status status) {
        this.shop = shop;
        this.categoryCode = categoryCode;
        this.eventType = eventType;
        this.regularPrice = regularPrice;
        this.discountPrice = discountPrice;
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdBy = createdBy;
        this.status = status;
    }
}
