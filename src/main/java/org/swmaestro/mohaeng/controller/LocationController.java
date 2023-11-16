package org.swmaestro.mohaeng.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.domain.user.auth.CustomUserDetails;
import org.swmaestro.mohaeng.dto.LocationCreateRequestDto;
import org.swmaestro.mohaeng.dto.LocationCreateResponseDto;
import org.swmaestro.mohaeng.service.LocationService;

import javax.validation.Valid;
import java.net.URI;


@Slf4j
@RequiredArgsConstructor
@RestController("/location")
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/create")
    public ResponseEntity<LocationCreateResponseDto> create(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody LocationCreateRequestDto locationCreateRequestDto) {

        log.info("create a new location: {}", locationCreateRequestDto.getAddress());

        User user = userDetails.getUser();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }
        LocationCreateResponseDto locationCreateResponseDto = locationService.save(user, locationCreateRequestDto);

        URI locationUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(locationCreateResponseDto.getId())
                .toUri();

        return ResponseEntity.created(locationUri).body(locationCreateResponseDto);
    }
}
