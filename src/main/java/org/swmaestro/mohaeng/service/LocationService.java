package org.swmaestro.mohaeng.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.dto.LocationCreateRequestDto;
import org.swmaestro.mohaeng.dto.LocationCreateResponseDto;
import org.swmaestro.mohaeng.repository.LocationRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationCreateResponseDto save(User user, LocationCreateRequestDto locationCreateRequestDto) {

        Location location = locationCreateRequestDto.toEntity(user);
        locationRepository.save(location);
        return LocationCreateResponseDto.builder()
                .id(location.getId())
                .name(location.getName())
                .address(location.getAddress())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build();
    }
}
