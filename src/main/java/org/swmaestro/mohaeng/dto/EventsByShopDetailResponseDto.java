package org.swmaestro.mohaeng.dto;

import lombok.Builder;
import lombok.Getter;
import org.swmaestro.mohaeng.domain.event.Event;
import org.swmaestro.mohaeng.domain.event.EventType;



@Getter
public class EventsByShopDetailResponseDto {

    private Long id;
    private String name;
    private String categoryCode;
    private EventType eventType;
    private int regularPrice;
    private int discountPrice;
    private String imageUrl;

    @Builder
    public EventsByShopDetailResponseDto(Long id, String name, String categoryCode, EventType eventType, int regularPrice, int discountPrice, String imageUrl) {
        this.id = id;
        this.name = name;
        this.categoryCode = categoryCode;
        this.eventType = eventType;
        this.regularPrice = regularPrice;
        this.discountPrice = discountPrice;
        this.imageUrl = imageUrl;
    }

    public static EventsByShopDetailResponseDto of(Event event) {
        return EventsByShopDetailResponseDto.builder()
                .id(event.getId())
                .name(event.getName())
                .categoryCode(event.getCategoryCode())
                .eventType(event.getEventType())
                .regularPrice(event.getRegularPrice())
                .discountPrice(event.getDiscountPrice())
                .imageUrl(event.getImageUrl())
                .build();
    }

}
