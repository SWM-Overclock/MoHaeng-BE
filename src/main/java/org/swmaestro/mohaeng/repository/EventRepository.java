package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.swmaestro.mohaeng.domain.event.Event;
import org.swmaestro.mohaeng.domain.event.EventType;

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
}
