package org.swmaestro.mohaeng.dto.shop;

import lombok.Builder;
import lombok.Getter;
import org.swmaestro.mohaeng.domain.shop.ShopDetail;
import org.swmaestro.mohaeng.domain.shop.ShopType;

@Getter
public class ShopDetailListRequestDto {

    private final Long id;
    private final ShopType shopType;
    private final String brandCode;
    private final double latitude;
    private final double longitude;
    private final String imageUrl;

    @Builder
    public ShopDetailListRequestDto(Long id, ShopType shopType, String brandCode, double latitude, double longitude, String imageUrl) {
        this.id = id;
        this.shopType = shopType;
        this.brandCode = brandCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
    }

    public static ShopDetailListRequestDto of(ShopDetail shopDetail) {
        return ShopDetailListRequestDto.builder()
                .id(shopDetail.getId())
                .shopType(shopDetail.getShop().getShopType())
                .brandCode(shopDetail.getShop().getBrandCode())
                .latitude(shopDetail.getLatitude())
                .longitude(shopDetail.getLongitude())
                .imageUrl(shopDetail.getImageUrl())
                .build();
    }
}
