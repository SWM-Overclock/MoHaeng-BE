package org.swmaestro.mohaeng.dto;

import lombok.Builder;
import lombok.Getter;
import org.swmaestro.mohaeng.domain.event.Event;
import org.swmaestro.mohaeng.domain.event.EventType;

@Getter
public class EventResponseDto {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private int regularPrice;
    private int discountPrice;
    private String startDate;
    private String endDate;
    private String categoryCode;
    private EventType eventType;

    public EventResponseDto(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.description = event.getDescription();
        this.imageUrl = event.getImageUrl();
        this.regularPrice = event.getRegularPrice();
        this.discountPrice = event.getDiscountPrice();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.categoryCode = event.getCategoryCode();
        this.eventType = event.getEventType();
    }

    public static EventResponseDto of(Event event) {
        return new EventResponseDto(event);
    }
}
