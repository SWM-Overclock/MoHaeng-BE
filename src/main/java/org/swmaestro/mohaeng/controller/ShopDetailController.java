package org.swmaestro.mohaeng.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.swmaestro.mohaeng.domain.user.auth.CustomUserDetails;
import org.swmaestro.mohaeng.dto.shop.ShopDetailResponseDto;
import org.swmaestro.mohaeng.dto.shop.ShopDetailListRequestDto;
import org.swmaestro.mohaeng.service.ShopDetailService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/shops")
public class ShopDetailController {

    private final ShopDetailService shopDetailService;

    @GetMapping("/nearby")
    public ResponseEntity<List<ShopDetailListRequestDto>> getNearbyShopDetail(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        log.info("Fetching nearby shops for user ID: {}", userId);
        List<ShopDetailListRequestDto> shopDetails = shopDetailService.getShopDetailsNearBy(userId);
        log.info("Number of shops found: {}", shopDetails.size());
        return ResponseEntity.ok(shopDetails);
    }

    @GetMapping("/{shopDetailId}")
    public ResponseEntity<ShopDetailResponseDto> getShopDetail(@PathVariable Long shopDetailId) {
        ShopDetailResponseDto shopDetailResponseDto = shopDetailService.getShopDetail(shopDetailId);
        return ResponseEntity.ok(shopDetailResponseDto);
    }
}
