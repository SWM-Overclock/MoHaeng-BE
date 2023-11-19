package org.swmaestro.mohaeng.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.swmaestro.mohaeng.dto.location.LocationDetailResponseDto;
import org.swmaestro.mohaeng.dto.location.LocationListResponseDto;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.dto.location.LocationCreateRequestDto;
import org.swmaestro.mohaeng.dto.location.LocationCreateResponseDto;
import org.swmaestro.mohaeng.repository.LocationRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional
    public LocationCreateResponseDto save(User user, LocationCreateRequestDto locationCreateRequestDto) {

        user.getLocations().forEach(location -> location.setPrimary(false));

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


    @Transactional(readOnly = true)
    public LocationDetailResponseDto getLocationById(User user, Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));

        log.info("{} {}", user, location.getUser());


        if (!location.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to access this location");
        }

        return LocationDetailResponseDto.of(location);
    }

    @Transactional
    public void deleteLocation(User user, Long locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "위치를 찾을 수 없습니다."));
        if (!location.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 위치를 삭제할 권한이 없습니다.");
        }
        locationRepository.delete(location);
    }

    @Transactional
    public void setPrimaryLocation(User user, Long locationId) {
        List<Location> locations = user.getLocations();
        Location primaryLocation = locations.stream()
                .filter(location -> location.getId().equals(locationId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "위치를 찾을 수 없습니다."));

        locations.forEach(loc -> loc.setPrimary(loc.equals(primaryLocation)));
    }

}
