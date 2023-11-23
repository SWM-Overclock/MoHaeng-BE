package org.swmaestro.mohaeng.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.swmaestro.mohaeng.dto.location.*;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.repository.LocationRepository;
import org.swmaestro.mohaeng.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;

    @Transactional
    public LocationCreateResponseDto save(Long userId, LocationCreateRequestDto locationCreateRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.getLocations().forEach(location -> {
            location.setPrimary(false);
            locationRepository.save(location);
        });

        Location newLocation = locationCreateRequestDto.toEntity(user);
        locationRepository.save(newLocation);

        user.addLocation(newLocation);

        return LocationCreateResponseDto.of(newLocation);
    }

    @Transactional(readOnly = true)
    public List<LocationListResponseDto> getAllLocations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user.getLocations().stream()
                .map(LocationListResponseDto::of)
                .toList();
    }


    @Transactional(readOnly = true)
    public LocationDetailResponseDto getLocationById(Long userId, Long locationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found"));


        if (!location.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have permission to access this location");
        }

        return LocationDetailResponseDto.of(location);
    }

    @Transactional
    public void deleteLocation(Long userId, Long locationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "위치를 찾을 수 없습니다."));

        if (!location.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 위치를 삭제할 권한이 없습니다.");
        }
        locationRepository.deleteById(location.getId());
        user.removeLocation(location);
        userRepository.save(user);
    }

    @Transactional
    public void setPrimaryLocation(Long userId, Long locationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        List<Location> locations = user.getLocations();
        Location primaryLocation = locations.stream()
                .filter(location -> location.getId().equals(locationId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "위치를 찾을 수 없습니다."));

        locations.forEach(loc -> {
            loc.setPrimary(loc.equals(primaryLocation));
            locationRepository.save(loc);
        });
        userRepository.save(user);
    }

    @Transactional
    public LocationDetailResponseDto updateLocation(Long userId, Long locationId, LocationUpdateRequestDto locationUpdateRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "위치를 찾을 수 없습니다."));

        if (!location.getUser().equals(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 위치를 수정할 권한이 없습니다.");
        }

        location.update(locationUpdateRequestDto);

        Location updatedLocation = locationRepository.save(location);
        return LocationDetailResponseDto.of(updatedLocation);
    }
}
