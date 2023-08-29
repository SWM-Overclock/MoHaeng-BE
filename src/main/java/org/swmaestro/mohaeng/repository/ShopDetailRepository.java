package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swmaestro.mohaeng.domain.shop.ShopDetail;

public interface ShopDetailRepository extends JpaRepository<ShopDetail, Long> {
}
