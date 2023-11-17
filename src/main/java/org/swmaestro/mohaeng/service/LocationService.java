package org.swmaestro.mohaeng.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public LocationCreateResponseDto save(User user, LocationCreateRequestDto locationCreateRequestDto) {

        Location newLocation = locationCreateRequestDto.toEntity(user);
        locationRepository.save(newLocation);

        user.getLocations().forEach(location -> location.setUsed(false));
        newLocation.setUsed(true);

        user.addLocation(newLocation);

        return LocationCreateResponseDto.builder()
                .id(newLocation.getId())
                .name(newLocation.getName())
                .address(newLocation.getAddress())
                .latitude(newLocation.getLatitude())
                .longitude(newLocation.getLongitude())
                .build();
    }
}
