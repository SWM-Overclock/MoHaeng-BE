package org.swmaestro.mohaeng.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.swmaestro.mohaeng.domain.shop.Shop;

import java.util.List;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    @Query(value = "SELECT DISTINCT sd.shop FROM shop_detail sd " +
            "WHERE ST_DISTANCE_SPHERE(" +
            "point(sd.shop_longitude, sd.shop_latitude), " +
            "point(:longitude, :latitude)) <= :radius " +
            "AND sd.status = 'ACTIVE'",
            nativeQuery = true)
    List<Shop> findDistinctShopsWithinRadius(@Param("longitude") double longitude, @Param("latitude") double latitude, @Param("radius") double radius);
}
