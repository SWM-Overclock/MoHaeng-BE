package org.swmaestro.mohaeng.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.domain.user.auth.CustomUserDetails;
import org.swmaestro.mohaeng.dto.EventListResponseDto;
import org.swmaestro.mohaeng.service.EventService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/events")
@RestController
public class EventController {

    private final EventService eventService;

    @GetMapping("/shops/{shopDetailId}")
    public ResponseEntity<Page<EventListResponseDto>> getEventsByShopDetail(@PathVariable Long shopDetailId, Pageable pageable) {
        Page<EventListResponseDto> events = eventService.getEventsByShopDetail(shopDetailId, pageable);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/")
    public ResponseEntity<Page<EventListResponseDto>> getAllEvents(@AuthenticationPrincipal CustomUserDetails userDetails, Pageable pageable) {
        User user = userDetails.getUser();
        Location userLocation = user.getPrimaryLocation();
        log.info("userLocation: {}", userLocation);

        Page<EventListResponseDto> events = eventService.getAllEvents(userLocation, pageable);
        return ResponseEntity.ok(events);
    }
}
