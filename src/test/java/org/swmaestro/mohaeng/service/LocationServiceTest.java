package org.swmaestro.mohaeng.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.dto.LocationCreateRequestDto;
import org.swmaestro.mohaeng.dto.LocationCreateResponseDto;
import org.swmaestro.mohaeng.dto.LocationDetailResponseDto;
import org.swmaestro.mohaeng.dto.LocationListResponseDto;
import org.swmaestro.mohaeng.repository.LocationRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    private Location location;
    private Long locationId = 1L;

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

    @Test
    public void testGetLocationById_Success() {
        // given
        Location location = Location.of(user, "Test Name", "Test Address", "37.422", "-122.084", true);
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));

        // when
        LocationDetailResponseDto responseDto = locationService.getLocationById(user, locationId);

        // then
        assertNotNull(responseDto);
        assertEquals("Test Name", responseDto.getName());
        assertEquals("Test Address", responseDto.getAddress());
        // Other assertions...
    }

    @Test
    public void testGetLocationById_NotFound() {
        // given
        when(locationRepository.findById(locationId)).thenReturn(Optional.empty());

        // when, then
        assertThrows(ResponseStatusException.class, () -> {
            locationService.getLocationById(user, locationId);
        });
    }

    @Test
    public void testGetLocationById_Forbidden() {
        // given
        String email = "test@mohaeng.org";
        String nickname = "test";
        String provider = "test";
        String providerId = "test";
        String imageUrl = "test";
        String name = "test";
        String address = "123 Test Street";
        String latitude = "37.422";
        String longitude = "-122.084";
        User anotherUser = User.createUser(email, nickname, provider, providerId, imageUrl);
        Location location = Location.of(user, name, address, latitude, longitude, true);
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));

        // when, then
        assertThrows(ResponseStatusException.class, () -> {
            locationService.getLocationById(anotherUser, locationId);
        });
    }
}