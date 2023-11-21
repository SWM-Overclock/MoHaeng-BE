package org.swmaestro.mohaeng.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.domain.user.auth.CustomUserDetails;
import org.swmaestro.mohaeng.dto.location.LocationCreateRequestDto;
import org.swmaestro.mohaeng.dto.location.LocationCreateResponseDto;
import org.swmaestro.mohaeng.dto.location.LocationDetailResponseDto;
import org.swmaestro.mohaeng.dto.location.LocationListResponseDto;
import org.swmaestro.mohaeng.service.LocationService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/location")
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/create")
    public ResponseEntity<LocationCreateResponseDto> create(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody LocationCreateRequestDto locationCreateRequestDto) {

        Long userId = userDetails.getUserId();
        log.info("userId: {}", userId);
        validateUser(userId);
        log.info("create a new location: {}", locationCreateRequestDto.getAddress());
        LocationCreateResponseDto locationCreateResponseDto = locationService.save(userId, locationCreateRequestDto);

        URI locationUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(locationCreateResponseDto.getId())
                .toUri();

        return ResponseEntity.created(locationUri).body(locationCreateResponseDto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<LocationListResponseDto>> getAllLocations(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        log.info("userId: {}", userId);
        validateUser(userId);

        List<LocationListResponseDto> locations = locationService.getAllLocations(userId);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDetailResponseDto> getLocation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @PathVariable Long locationId) {
        Long userId = userDetails.getUserId();
        log.info("userId: {}", userId);
        validateUser(userId);

        log.info("locationId: {}", locationId);
        LocationDetailResponseDto location = locationService.getLocationById(userId, locationId);
        return ResponseEntity.ok(location);
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> deleteLocation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @PathVariable Long locationId) {
        Long userId = userDetails.getUserId();
        validateUser(userId);
        log.info("ID: {}인 위치 삭제 중", locationId);
        locationService.deleteLocation(userId, locationId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/primary/{locationId}")
    public ResponseEntity<Void> setPrimaryLocation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @PathVariable Long locationId) {
        Long userId = userDetails.getUserId();
        validateUser(userId);
        log.info("ID: {}인 위치를 주 위치로 설정 중", locationId);
        locationService.setPrimaryLocation(userId, locationId);
        return ResponseEntity.ok().build();
    }

    private static void validateUser(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }
}
