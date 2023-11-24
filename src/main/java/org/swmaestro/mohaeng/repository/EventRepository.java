package org.swmaestro.mohaeng.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.swmaestro.mohaeng.domain.event.Event;
import org.swmaestro.mohaeng.domain.event.EventType;
import org.swmaestro.mohaeng.domain.shop.Shop;

import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT COUNT(e) " +
            "FROM Event e " +
            "WHERE e.shop.id = (SELECT sd.shop.id " +
            "FROM ShopDetail sd " +
            "WHERE sd.id = :shopDetailId)")
    Long countByShopDetail(@Param("shopDetailId") Long shopDetailId);

    @Query("SELECT DISTINCT e.eventType FROM Event e WHERE e.shop.id = (SELECT sd.shop.id FROM ShopDetail sd WHERE sd.id = :shopDetailId)")
    Set<EventType> findDistinctEventDetailsByShopDetailId(@Param("shopDetailId") Long shopDetailId);

    @Query("SELECT e FROM Event e WHERE e.shop.id = (SELECT sd.shop.id FROM ShopDetail sd WHERE sd.id = :shopDetailId)")
    Page<Event> findByShopDetailIdWithPagination(@Param("shopDetailId") Long shopDetailId, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.shop IN :shops")
    Page<Event> findByShopsWithPagination(@Param("shops") List<Shop> shops, Pageable pageable);
}
