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
import org.swmaestro.mohaeng.dto.LocationListResponseDto;
import org.swmaestro.mohaeng.repository.LocationRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private User user;

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

    @Test
    public void testGetAllLocations() {

        // given
        String locationName = "Test Name";
        String address = "Test Address";
        String latitude = "37.422";
        String longitude = "-122.084";

        Location location1 = Location.of(user, locationName, address, latitude, longitude, true);
        Location location2 = Location.of(user, locationName, address, latitude, longitude, true);
        when(user.getLocations()).thenReturn(Arrays.asList(location1, location2));

        // when
        List<LocationListResponseDto> result = locationService.getAllLocations(user);

        // then
        assertEquals(2, result.size());
        result.forEach(dto -> {
            assertEquals(locationName, dto.getName());
            assertEquals(address, dto.getAddress());
            assertTrue(dto.getIsUsed() != null);
        });
    }

}