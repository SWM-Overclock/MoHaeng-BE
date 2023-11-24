package org.swmaestro.mohaeng.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.shop.Shop;
import org.swmaestro.mohaeng.dto.EventListResponseDto;
import org.swmaestro.mohaeng.dto.EventResponseDto;
import org.swmaestro.mohaeng.repository.EventRepository;
import org.swmaestro.mohaeng.repository.ShopRepository;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class EventService {

    private static final int RADIUS = 1000;

    private final EventRepository eventRepository;
    private final ShopRepository shopRepository;

    @Transactional(readOnly = true)
    public Page<EventListResponseDto> getEventsByShopDetail(Long shopDetailId, Pageable pageable) {
        return eventRepository.findByShopDetailIdWithPagination(shopDetailId, pageable)
                .map(event -> EventListResponseDto.of(event));
    }

    @Transactional(readOnly = true)
    public Page<EventListResponseDto> getAllEvents(Location userLocation, Pageable pageable) {

        List<Shop> shops = shopRepository.findDistinctShopsWithinRadius(
                userLocation.getLongitude(),
                userLocation.getLatitude(),
                RADIUS
        );

        return eventRepository.findByShopsWithPagination(shops, pageable)
                .map(event -> EventListResponseDto.of(event));
    }

    @Transactional(readOnly = true)
    public EventResponseDto getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .map(event -> EventResponseDto.of(event))
                .orElseThrow(() -> new IllegalArgumentException("해당 이벤트가 없습니다."));
    }
}
