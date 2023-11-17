package org.swmaestro.mohaeng.dto;

import org.junit.jupiter.api.Test;
import org.swmaestro.mohaeng.domain.Location;

import static org.junit.jupiter.api.Assertions.*;

class LocationCreateResponseDtoTest {

    @Test
    void testLocationCreateResponseDto() {
        // given
        Long id = 1L;
        String name = "Test Name";
        String address = "Test Address";
        String latitude = "37.422";
        String longitude = "-122.084";
        boolean isUsed = true;

        Location location = Location.of(null, name, address, latitude, longitude, isUsed);

        // when
        LocationCreateResponseDto responseDto = LocationCreateResponseDto.of(location);

        // then
        assertNotNull(responseDto);
        assertEquals(name, responseDto.getName());
        assertEquals(address, responseDto.getAddress());
        assertEquals(latitude, responseDto.getLatitude());
        assertEquals(longitude, responseDto.getLongitude());
        assertEquals(isUsed, responseDto.isUsed());
    }
}