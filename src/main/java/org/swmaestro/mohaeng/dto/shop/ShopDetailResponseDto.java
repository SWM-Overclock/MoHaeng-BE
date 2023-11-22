package org.swmaestro.mohaeng.dto.shop;

import lombok.Builder;
import lombok.Getter;
import org.swmaestro.mohaeng.domain.event.EventType;
import org.swmaestro.mohaeng.domain.shop.ShopDetail;
import org.swmaestro.mohaeng.domain.shop.ShopType;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ShopDetailResponseDto {

    private final Long id;
    private final String name;
    private final ShopType shopType;
    private final String brandCode;
    private final double latitude;
    private final double longitude;
    private final String imageUrl;
    private final Long eventCount;
    private final Set<String> eventTypes;

    @Builder
    public ShopDetailResponseDto(Long id, String name, ShopType shopType, String brandCode, double latitude, double longitude, String imageUrl, Long eventCount, Set<EventType> eventTypes) {
        this.id = id;
        this.name = name;
        this.shopType = shopType;
        this.brandCode = brandCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
        this.eventCount = eventCount;
        this.eventTypes = eventTypes.stream()
                .map(EventType::getValue)
                .collect(Collectors.toSet());
    }

    public static ShopDetailResponseDto of(ShopDetail shopDetail, Long eventCount, Set<EventType> eventTypes) {
        return ShopDetailResponseDto.builder()
                .id(shopDetail.getId())
                .name(shopDetail.getName())
                .shopType(shopDetail.getShop().getShopType())
                .brandCode(shopDetail.getShop().getBrandCode())
                .latitude(shopDetail.getLatitude())
                .longitude(shopDetail.getLongitude())
                .imageUrl(shopDetail.getImageUrl())
                .eventCount(eventCount)
                .eventTypes(eventTypes)
                .build();
    }
}
