package org.swmaestro.mohaeng.dto.shop;

import lombok.Builder;
import lombok.Getter;
import org.swmaestro.mohaeng.domain.shop.ShopDetail;
import org.swmaestro.mohaeng.domain.shop.ShopType;

@Getter
public class ShopDetailListRequestDto {

    private final Long id;
    private final String name;
    private final ShopType shopType;
    private final String brandCode;
    private final double latitude;
    private final double longitude;

    @Builder
    public ShopDetailListRequestDto(Long id, String name, ShopType shopType, String brandCode, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.shopType = shopType;
        this.brandCode = brandCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static ShopDetailListRequestDto of(ShopDetail shopDetail) {
        return ShopDetailListRequestDto.builder()
                .id(shopDetail.getId())
                .name(shopDetail.getName())
                .shopType(shopDetail.getShop().getShopType())
                .brandCode(shopDetail.getShop().getBrandCode())
                .latitude(shopDetail.getLatitude())
                .longitude(shopDetail.getLongitude())
                .build();
    }
}
