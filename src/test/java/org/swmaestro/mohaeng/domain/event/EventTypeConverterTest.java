package org.swmaestro.mohaeng.domain.event;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTypeConverterTest {

    private final EventTypeConverter converter = new EventTypeConverter();
    
    @Test
    void convertToDatabaseColumn() {
        assertEquals("1+1", converter.convertToDatabaseColumn(EventType.ONE_PLUS_ONE));
        assertEquals("2+1", converter.convertToDatabaseColumn(EventType.TWO_PLUS_ONE));
        assertEquals("가격할인", converter.convertToDatabaseColumn(EventType.PRICE_DISCOUNT));
        assertEquals("사은품증정", converter.convertToDatabaseColumn(EventType.GIFT));
        assertEquals("쿠폰", converter.convertToDatabaseColumn(EventType.COUPON));
        assertEquals("기타", converter.convertToDatabaseColumn(EventType.ETC));
    }

    @Test
    void convertToEntityAttribute() {
        assertEquals(EventType.ONE_PLUS_ONE, converter.convertToEntityAttribute("1+1"));
        assertEquals(EventType.TWO_PLUS_ONE, converter.convertToEntityAttribute("2+1"));
        assertEquals(EventType.PRICE_DISCOUNT, converter.convertToEntityAttribute("가격할인"));
        assertEquals(EventType.GIFT, converter.convertToEntityAttribute("사은품증정"));
        assertEquals(EventType.COUPON, converter.convertToEntityAttribute("쿠폰"));
        assertEquals(EventType.ETC, converter.convertToEntityAttribute("기타"));
    }

    @Test
    void convertNull() {
        assertNull(converter.convertToDatabaseColumn(null));
        assertNull(converter.convertToEntityAttribute(null));
    }

    @Test
    void shouldThrowExceptionForUnknownValue() {
        assertThrows(IllegalArgumentException.class, () -> converter.convertToEntityAttribute("Unknown"));
    }
}