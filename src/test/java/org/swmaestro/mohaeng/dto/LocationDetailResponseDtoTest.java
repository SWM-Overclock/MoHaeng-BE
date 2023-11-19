package org.swmaestro.mohaeng.dto;

import org.junit.jupiter.api.Test;
import org.swmaestro.mohaeng.domain.Location;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LocationDetailResponseDtoTest {

    @Test
    public void testLocationDetailResponseDto() {
        Location mockLocation = mock(Location.class);

        Long expectedId = 1L;
        String expectedName = "Test Location";
        String expectedAddress = "123 Test St";
        String expectedLatitude = "40.7128";
        String expectedLongitude = "-74.0060";
        boolean expectedIsUsed = true;

        when(mockLocation.getId()).thenReturn(expectedId);
        when(mockLocation.getName()).thenReturn(expectedName);
        when(mockLocation.getAddress()).thenReturn(expectedAddress);
        when(mockLocation.getLatitude()).thenReturn(expectedLatitude);
        when(mockLocation.getLongitude()).thenReturn(expectedLongitude);
        when(mockLocation.getIsUsed()).thenReturn(expectedIsUsed);

        LocationDetailResponseDto dto = LocationDetailResponseDto.of(mockLocation);

        assertEquals(expectedId, dto.getId());
        assertEquals(expectedName, dto.getName());
        assertEquals(expectedAddress, dto.getAddress());
        assertEquals(expectedLatitude, dto.getLatitude());
        assertEquals(expectedLongitude, dto.getLongitude());
        assertEquals(expectedIsUsed, dto.isUsed());
    }
}
