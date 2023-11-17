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
import org.swmaestro.mohaeng.dto.LocationCreateRequestDto;
import org.swmaestro.mohaeng.dto.LocationCreateResponseDto;
import org.swmaestro.mohaeng.dto.LocationDetailResponseDto;
import org.swmaestro.mohaeng.dto.LocationListResponseDto;
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

        User user = userDetails.getUser();
        validateUser(user);
        log.info("create a new location: {}", locationCreateRequestDto.getAddress());
        LocationCreateResponseDto locationCreateResponseDto = locationService.save(user, locationCreateRequestDto);

        URI locationUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(locationCreateResponseDto.getId())
                .toUri();

        return ResponseEntity.created(locationUri).body(locationCreateResponseDto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<LocationListResponseDto>> getAllLocations(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        log.info("user: {}", user);
        validateUser(user);

        List<LocationListResponseDto> locations = locationService.getAllLocations(user);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<LocationDetailResponseDto> getLocation(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                 @PathVariable Long locationId) {
        User user = userDetails.getUser();
        validateUser(user);
        log.info("locationId: {}", locationId);
        LocationDetailResponseDto location = locationService.getLocationById(user, locationId);
        return ResponseEntity.ok(location);
    }

    private static void validateUser(User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
    }
}
