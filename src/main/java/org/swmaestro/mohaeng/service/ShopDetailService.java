package org.swmaestro.mohaeng.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.swmaestro.mohaeng.domain.Location;
import org.swmaestro.mohaeng.domain.event.EventType;
import org.swmaestro.mohaeng.domain.shop.ShopDetail;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.dto.shop.ShopDetailListRequestDto;
import org.swmaestro.mohaeng.dto.shop.ShopDetailResponseDto;
import org.swmaestro.mohaeng.repository.EventRepository;
import org.swmaestro.mohaeng.repository.ShopDetailRepository;
import org.swmaestro.mohaeng.repository.UserRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShopDetailService {

    private static final int RADIUS = 1000;

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ShopDetailRepository shopDetailRepository;

    @Transactional(readOnly = true)
    public List<ShopDetailListRequestDto> getShopDetailsNearBy(Long userId) {
        Location userLocation = getUserPrimaryLocation(userId);
        log.info("userLocation: {}", userLocation.getAddress());
        List<ShopDetail> shopDetails = shopDetailRepository.findShopDetailsWithinRadius(
                userLocation.getLongitude(),
                userLocation.getLatitude(),
                RADIUS
        );

        return shopDetails.stream()
                .map(shopDetail -> ShopDetailListRequestDto.of(shopDetail))
                .collect(Collectors.toList());
    }

    private Location getUserPrimaryLocation(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
        return user.getPrimaryLocation();
    }

    @Transactional(readOnly = true)
    public ShopDetailResponseDto getShopDetail(Long shopDetailId) {
        ShopDetail shopDetail = shopDetailRepository.findById(shopDetailId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 없습니다."));
        Long eventCount = eventRepository.countByShopDetail(shopDetailId);
        Set<EventType> eventTypes = eventRepository.findDistinctEventDetailsByShopDetailId(shopDetailId);
        return ShopDetailResponseDto.of(shopDetail, eventCount, eventTypes);
    }
}
