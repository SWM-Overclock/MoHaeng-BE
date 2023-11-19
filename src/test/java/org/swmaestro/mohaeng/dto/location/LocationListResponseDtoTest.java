package org.swmaestro.mohaeng.dto.location;

import org.junit.jupiter.api.Test;
import org.swmaestro.mohaeng.domain.Location;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LocationListResponseDtoTest {

    @Test
    public void testOfMethod() {
        // given
        Location location = Location.of(null, "Test Name", "Test Address", "37.422", "-122.084", true);

        // when
        LocationListResponseDto dto = LocationListResponseDto.of(location);

        // then
        assertEquals(location.getId(), dto.getId());
        assertEquals(location.getName(), dto.getName());
        assertEquals(location.getAddress(), dto.getAddress());
        assertEquals(location.getIsPrimary(), dto.getIsPrimary());
    }
}
