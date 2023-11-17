package org.swmaestro.mohaeng.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.dto.LocationCreateRequestDto;
import org.swmaestro.mohaeng.dto.LocationCreateResponseDto;
import org.swmaestro.mohaeng.repository.LocationRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    @Test
    public void testSaveLocation() {

        // given
        String email = "test@mohaeng.org";
        String password = "test";
        String name = "test";
        String address = "123 Test Street";
        String latitude = "37.422";
        String longitude = "-122.084";
        User user = User.createUser(email, password, name, name, name);
        LocationCreateRequestDto locationCreateRequestDto = new LocationCreateRequestDto(name, address, latitude, longitude);

        when(locationRepository.save(any(Location.class))).thenAnswer(i -> i.getArguments()[0]);

        // when
        LocationCreateResponseDto responseDto = locationService.save(user, locationCreateRequestDto);

        // then
        assertNotNull(responseDto);
        assertEquals(name, responseDto.getName());
        assertEquals(address, responseDto.getAddress());
        assertEquals(latitude, responseDto.getLatitude());
        assertEquals(longitude, responseDto.getLongitude());

        verify(locationRepository, times(1)).save(any(Location.class));
    }

}