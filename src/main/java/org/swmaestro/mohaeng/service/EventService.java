package org.swmaestro.mohaeng.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.swmaestro.mohaeng.dto.EventsByShopDetailResponseDto;
import org.swmaestro.mohaeng.repository.EventRepository;


@Slf4j
@RequiredArgsConstructor
@Service
public class EventService {

    private final EventRepository eventRepository;

    public Page<EventsByShopDetailResponseDto> getEventsByShopDetail(Long shopDetailId, Pageable pageable) {
        return eventRepository.findByShopDetailIdWithPagination(shopDetailId, pageable)
                .map(event -> EventsByShopDetailResponseDto.of(event));
    }
}
