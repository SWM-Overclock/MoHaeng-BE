package org.swmaestro.mohaeng.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swmaestro.mohaeng.dto.EventsByShopDetailResponseDto;
import org.swmaestro.mohaeng.service.EventService;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/events")
@RestController
public class EventController {

    private final EventService eventService;

    @GetMapping("/shops/{shopDetailId}")
    public ResponseEntity<Page<EventsByShopDetailResponseDto>> getEventsByShopDetail(@PathVariable Long shopDetailId, Pageable pageable) {
        Page<EventsByShopDetailResponseDto> events = eventService.getEventsByShopDetail(shopDetailId, pageable);
        return ResponseEntity.ok(events);
    }
}
