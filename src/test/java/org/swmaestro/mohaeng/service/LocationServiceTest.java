package org.swmaestro.mohaeng.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.dto.location.LocationCreateRequestDto;
import org.swmaestro.mohaeng.dto.location.LocationCreateResponseDto;
import org.swmaestro.mohaeng.dto.location.LocationDetailResponseDto;
import org.swmaestro.mohaeng.dto.location.LocationListResponseDto;
import org.swmaestro.mohaeng.repository.LocationRepository;
import org.swmaestro.mohaeng.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LocationService locationService;

    private final Long userId = 1L;
    private final Long locationId = 1L;

    @Test
    public void saveLocationTest() {
        // given
        User user = mock(User.class);
        LocationCreateRequestDto requestDto = new LocationCreateRequestDto("Home", "123 Main Street", 10.0, 20.0);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(locationRepository.save(any(Location.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        LocationCreateResponseDto response = locationService.save(userId, requestDto);

        // then
        assertNotNull(response);
        verify(locationRepository, times(1)).save(any(Location.class));
    }

    @Test
    public void getAllLocationsTest() {
        // given
        User user = mock(User.class);
        Location location = new Location(user, "Home", "123 Main Street", 10.0, 20.0, false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getLocations()).thenReturn(Collections.singletonList(location));

        // when
        List<LocationListResponseDto> locations = locationService.getAllLocations(userId);

        // then
        assertNotNull(locations);
        assertFalse(locations.isEmpty());
        assertEquals(1, locations.size());
    }

    @Test
    public void getLocationByIdTest() {
        // given
        User user = mock(User.class);
        Location location = new Location(user, "Home", "123 Main Street", 10.0, 20.0, false);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));

        // when
        LocationDetailResponseDto response = locationService.getLocationById(userId, locationId);

        // then
        assertNotNull(response);
        assertEquals("Home", response.getName());
    }

    @Test
    public void deleteLocationTest() {
        // given
        User user = mock(User.class);
        Location location = mock(Location.class); // Location 객체를 모킹합니다.
        when(location.getId()).thenReturn(locationId); // getId() 호출 시 locationId를 반환하도록 설정합니다.
        when(location.getUser()).thenReturn(user); // getUser() 호출 시 mock된 user를 반환하도록 설정합니다.
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(locationRepository.findById(locationId)).thenReturn(Optional.of(location));

        // when
        locationService.deleteLocation(userId, locationId);

        // then
        verify(locationRepository, times(1)).deleteById(locationId);
    }


    @Test
    public void setPrimaryLocationTest() {
        // given
        User user = mock(User.class);
        Location location = mock(Location.class); // Location을 모킹합니다.
        when(location.getId()).thenReturn(locationId); // getId() 호출 시 locationId를 반환하도록 설정합니다.
        List<Location> locations = Arrays.asList(location);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getLocations()).thenReturn(locations);

        // when
        locationService.setPrimaryLocation(userId, locationId);

        // then
        verify(locationRepository, times(locations.size())).save(any(Location.class));
    }
}