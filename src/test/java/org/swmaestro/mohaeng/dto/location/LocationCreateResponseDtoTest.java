package org.swmaestro.mohaeng.dto.location;

import org.junit.jupiter.api.Test;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.dto.location.LocationCreateResponseDto;

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
        boolean isPrimary = true;

        Location location = Location.of(null, name, address, latitude, longitude, isPrimary);

        // when
        LocationCreateResponseDto responseDto = LocationCreateResponseDto.of(location);

        // then
        assertNotNull(responseDto);
        assertEquals(name, responseDto.getName());
        assertEquals(address, responseDto.getAddress());
        assertEquals(latitude, responseDto.getLatitude());
        assertEquals(longitude, responseDto.getLongitude());
        assertEquals(isPrimary, responseDto.isPrimary());
    }
}