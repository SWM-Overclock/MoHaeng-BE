package org.swmaestro.mohaeng.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.swmaestro.mohaeng.dto.LocationDetailResponseDto;
import org.swmaestro.mohaeng.dto.LocationListResponseDto;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.dto.LocationCreateRequestDto;
import org.swmaestro.mohaeng.dto.LocationCreateResponseDto;
import org.swmaestro.mohaeng.repository.LocationRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional
    public LocationCreateResponseDto save(User user, LocationCreateRequestDto locationCreateRequestDto) {

        user.getLocations().forEach(location -> location.setUsed(false));

        Location newLocation = locationCreateRequestDto.toEntity(user);
        locationRepository.save(newLocation);

        user.addLocation(newLocation);

        return LocationCreateResponseDto.of(newLocation);
    }

    @Transactional(readOnly = true)
    public List<LocationListResponseDto> getAllLocations(User user) {
        return user.getLocations().stream()
                .map(LocationListResponseDto::of)
                .toList();
    }

    public LocationDetailResponseDto getLocationById(User user, Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));

        log.info("{} {}", user, location.getUser());


        if (!location.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to access this location");
        }

        return LocationDetailResponseDto.of(location);
    }
}
