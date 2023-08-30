package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.swmaestro.mohaeng.domain.shop.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {
}
