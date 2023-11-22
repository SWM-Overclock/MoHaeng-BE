package org.swmaestro.mohaeng.domain.event;

import lombok.Getter;

@Getter
public enum EventType {
    ONE_PLUS_ONE("1+1"),
    TWO_PLUS_ONE("2+1"),
    PRICE_DISCOUNT("가격할인"),
    GIFT("사은품증정"),
    COUPON("쿠폰"),
    ETC("기타");

    private final String value;

    private EventType(String value) {
        this.value = value;
    }
}
