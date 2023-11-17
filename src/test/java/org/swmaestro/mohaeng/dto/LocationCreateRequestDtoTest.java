package org.swmaestro.mohaeng.dto;

import org.junit.jupiter.api.Test;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.user.User;

import static org.junit.jupiter.api.Assertions.*;

class LocationCreateRequestDtoTest {

    @Test
    void testLocationCreateRequestDto() {
        // given
        String email = "test@mohaeng.org";
        String nickname = "testNickname";
        String provider = "testProvider";
        String providerId = "testProviderId";
        String imageUrl = "testImageUrl";
        String locationName = "Test Name";
        String address = "Test Address";
        String latitude = "37.422";
        String longitude = "-122.084";

        User user = User.createUser(email, nickname, provider, providerId, imageUrl);

        // when
        LocationCreateRequestDto requestDto = new LocationCreateRequestDto(locationName, address, latitude, longitude);
        Location location = requestDto.toEntity(user);

        // then
        assertNotNull(location);
        assertEquals(locationName, location.getName());
        assertEquals(address, location.getAddress());
        assertEquals(latitude, location.getLatitude());
        assertEquals(longitude, location.getLongitude());
    }
}